package com.fernando.manantial_ms_customers.infrastructure.adapters.input.rest;

import com.fernando.manantial_ms_customers.application.ports.input.GetCustomersUseCase;
import com.fernando.manantial_ms_customers.infrastructure.adapters.input.rest.mappers.CustomerRestMapper;
import com.fernando.manantial_ms_customers.infrastructure.adapters.input.rest.models.response.CustomerResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/customers")
@Tag(name = "Customers", description = "Operations related to customers")
public class CustomerRestAdapter {

    private final GetCustomersUseCase getCustomersUseCase;
    private final CustomerRestMapper customerRestMapper;

    @GetMapping
    @Operation(summary = "Find all customer available")
    @ApiResponse(responseCode = "200",description = "A lists customers available")
    public Flux<CustomerResponse> getCustomers(){
        return customerRestMapper.customerFluxToCustomerResponseFlux(getCustomersUseCase.getCustomers());
    }
}
