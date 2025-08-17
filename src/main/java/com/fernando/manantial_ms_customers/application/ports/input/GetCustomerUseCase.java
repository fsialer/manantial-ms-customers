package com.fernando.manantial_ms_customers.application.ports.input;

import com.fernando.manantial_ms_customers.domain.models.Customer;
import reactor.core.publisher.Mono;

public interface GetCustomerUseCase {
    Mono<Customer> getCustomer(String id);
}
