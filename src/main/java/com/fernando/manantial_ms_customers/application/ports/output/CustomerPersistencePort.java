package com.fernando.manantial_ms_customers.application.ports.output;

import com.fernando.manantial_ms_customers.domain.models.Customer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerPersistencePort {
    Flux<Customer> getCustomers();
    Mono<Customer> saveCustomer(Customer customer);
}
