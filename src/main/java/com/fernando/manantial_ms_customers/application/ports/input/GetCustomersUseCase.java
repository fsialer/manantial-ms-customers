package com.fernando.manantial_ms_customers.application.ports.input;

import com.fernando.manantial_ms_customers.domain.models.Customer;
import reactor.core.publisher.Flux;

public interface GetCustomersUseCase {
    Flux<Customer> getCustomers();
}
