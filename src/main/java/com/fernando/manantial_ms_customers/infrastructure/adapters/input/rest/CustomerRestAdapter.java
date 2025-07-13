package com.fernando.manantial_ms_customers.infrastructure.adapters.input.rest;

import com.fernando.manantial_ms_customers.application.ports.input.GetCustomersUseCase;
import com.fernando.manantial_ms_customers.application.ports.input.SaveCustomerUseCase;
import com.fernando.manantial_ms_customers.infrastructure.adapters.input.rest.mappers.CustomerRestMapper;
import com.fernando.manantial_ms_customers.infrastructure.adapters.input.rest.models.request.CustomerRequest;
import com.fernando.manantial_ms_customers.infrastructure.adapters.input.rest.models.response.CustomerResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/customers")
@Tag(name = "Customers", description = "Operations related to customers")
public class CustomerRestAdapter {

    private final GetCustomersUseCase getCustomersUseCase;
    private final SaveCustomerUseCase saveCustomerUseCase;
    private final CustomerRestMapper customerRestMapper;

    @GetMapping
    @Operation(summary = "Find all customer available")
    @ApiResponse(responseCode = "200",description = "A lists customers available")
    public Flux<CustomerResponse> getCustomers(){
        return customerRestMapper.customerFluxToCustomerResponseFlux(getCustomersUseCase.getCustomers());
    }

    @PostMapping
    @Operation(summary = "Save customer")
    @ApiResponse(responseCode = "201", description = "Customer saved correctly")
    @ApiResponse(responseCode = "400", description = "Customer failed to the save by some field")
    public Mono<ResponseEntity<CustomerResponse>> saveCustomer(@Valid @RequestBody CustomerRequest rq){
        return saveCustomerUseCase.save(customerRestMapper.customerRequestToCustomer(rq)).flatMap(
                customer ->{
                        String location = "/v1/customers/".concat(customer.getId());
                        return Mono.just(ResponseEntity.created(URI.create(location)).body(customerRestMapper.customerToCustomerResponse(customer)));
                }
        );
    }
}
