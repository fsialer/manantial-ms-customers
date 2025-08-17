package com.fernando.manantial_ms_customers.infrastructure.adapters.input.rest;

import com.fernando.manantial_ms_customers.application.ports.input.*;
import com.fernando.manantial_ms_customers.infrastructure.adapters.input.rest.mappers.CustomerRestMapper;
import com.fernando.manantial_ms_customers.infrastructure.adapters.input.rest.models.request.CustomerRequest;
import com.fernando.manantial_ms_customers.infrastructure.adapters.input.rest.models.response.CustomerResponse;
import com.fernando.manantial_ms_customers.infrastructure.adapters.input.rest.models.response.MetricResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    private final GetMetricsUseCase getMetricsUseCase;
    private final UpdateCustomerUseCase updateCustomerUseCase;
    private final DeleteCustomerUseCase deleteCustomerUseCase;
    private final GetCustomerUseCase getCustomerUseCase;

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

    @GetMapping("/metrics")
    @Operation(summary = "Get metrics")
    @ApiResponse(responseCode = "200", description = "Obtain metric about average and standard Deviation of age customers")
    public Mono<ResponseEntity<MetricResponse>> getMetrics(){
        return getMetricsUseCase.getMetrics().flatMap(metrics->Mono.just(ResponseEntity.ok(customerRestMapper.metricToMetricResponse(metrics))));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update customer")
    @ApiResponse(responseCode = "200", description = "Customer updated correctly")
    @ApiResponse(responseCode = "400", description = "Customer failed to the update by some field")
    @ApiResponse(responseCode = "404", description = "Customer not found")
    public Mono<ResponseEntity<CustomerResponse>> updateCustomer(@PathVariable("id") String id,  @Valid @RequestBody CustomerRequest rq){
        return updateCustomerUseCase.update(id,customerRestMapper.customerRequestToCustomer(rq)).flatMap(
                customer ->Mono.just(ResponseEntity.ok(customerRestMapper.customerToCustomerResponse(customer))));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete customer")
    @ApiResponse(responseCode = "204", description = "Customer deleted correctly")
    @ApiResponse(responseCode = "404", description = "Customer not found")
    public Mono<Void> deleteCustomer(@PathVariable("id") String id){
        return deleteCustomerUseCase.delete(id);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get customer")
    @ApiResponse(responseCode = "200", description = "Customer information")
    @ApiResponse(responseCode = "404", description = "Customer not found")
    public Mono<ResponseEntity<CustomerResponse>> getCustomer(@PathVariable("id") String id){
        return getCustomerUseCase.getCustomer(id).flatMap(
                customer ->  Mono.just(ResponseEntity.ok(customerRestMapper.customerToCustomerResponse(customer))));
    }
}
