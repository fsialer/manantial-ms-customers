package com.fernando.manantial_ms_customers.infrastructure.adapters.input.rest.mappers;

import com.fernando.manantial_ms_customers.domain.models.Customer;
import com.fernando.manantial_ms_customers.infrastructure.adapters.input.rest.models.request.CustomerRequest;
import com.fernando.manantial_ms_customers.infrastructure.adapters.input.rest.models.response.CustomerResponse;
import reactor.core.publisher.Flux;

public interface CustomerRestMapper {
    Flux<CustomerResponse> customerFluxToCustomerResponseFlux(Flux<Customer> customerFlux);
    Customer customerRequestToCustomer(CustomerRequest customerRequest);
    CustomerResponse customerToCustomerResponse(Customer customer);
}
