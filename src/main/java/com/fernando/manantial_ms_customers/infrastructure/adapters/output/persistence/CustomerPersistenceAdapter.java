package com.fernando.manantial_ms_customers.infrastructure.adapters.output.persistence;

import com.fernando.manantial_ms_customers.application.ports.output.CustomerPersistencePort;
import com.fernando.manantial_ms_customers.domain.models.Customer;
import com.fernando.manantial_ms_customers.infrastructure.adapters.output.persistence.mappers.CustomerPersistenceMapper;
import com.fernando.manantial_ms_customers.infrastructure.adapters.output.persistence.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CustomerPersistenceAdapter implements CustomerPersistencePort {
    private CustomerPersistenceMapper customerPersistenceMapper;
    private CustomerRepository customerRepository;
    @Override
    public Flux<Customer> getCustomers() {
        return customerPersistenceMapper.customerDocumenFluxtoToCustomerFlux(customerRepository.findAll());
    }

    @Override
    public Mono<Customer> saveCustomer() {
        return null;
    }
}
