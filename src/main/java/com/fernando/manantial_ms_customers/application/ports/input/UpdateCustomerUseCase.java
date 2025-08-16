package com.fernando.manantial_ms_customers.application.ports.input;

import com.fernando.manantial_ms_customers.domain.models.Customer;
import reactor.core.publisher.Mono;

public interface UpdateCustomerUseCase {
    Mono<Customer> update(String id, Customer customer);
}
