package com.fernando.manantial_ms_customers.domain.services;

import com.fernando.manantial_ms_customers.application.ports.input.GetCustomersUseCase;
import com.fernando.manantial_ms_customers.application.ports.output.CustomerPersistencePort;
import com.fernando.manantial_ms_customers.domain.models.Customer;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class CustomerServices implements GetCustomersUseCase {

    private final CustomerPersistencePort customerPersistencePort;
    @Override
    public Flux<Customer> getCustomers() {
        return customerPersistencePort.getCustomers();
    }
}
