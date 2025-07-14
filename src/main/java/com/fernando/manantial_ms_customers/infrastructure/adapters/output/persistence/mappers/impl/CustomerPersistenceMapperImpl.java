package com.fernando.manantial_ms_customers.infrastructure.adapters.output.persistence.mappers.impl;

import com.fernando.manantial_ms_customers.domain.models.Customer;
import com.fernando.manantial_ms_customers.infrastructure.adapters.output.persistence.mappers.CustomerPersistenceMapper;
import com.fernando.manantial_ms_customers.infrastructure.adapters.output.persistence.models.CustomerDocument;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class CustomerPersistenceMapperImpl implements CustomerPersistenceMapper {
    public Flux<Customer> customerDocumenFluxtoToCustomerFlux(Flux<CustomerDocument> customerDocumentFlux){
        return customerDocumentFlux.flatMap(customer->
             Flux.just(
                    Customer.builder()
                            .id(customer.getId())
                            .name(customer.getName())
                            .lastName(customer.getLastName())
                            .age(customer.getAge())
                            .birthDate(customer.getBirthDate())
                            .build()
            )
        );

    }

    @Override
    public CustomerDocument customerToCustomerDocument(Customer customer) {
        return CustomerDocument.builder()
                .name(customer.getName())
                .lastName(customer.getLastName())
                .age(customer.getAge())
                .birthDate(customer.getBirthDate())
                .build();
    }

    @Override
    public Mono<Customer> customerDocumentMonoToCustomerMono(Mono<CustomerDocument> customerDocumentMono) {
        return customerDocumentMono.flatMap(customer->
            Mono.just(Customer.builder()
                            .id(customer.getId())
                            .name(customer.getName())
                            .lastName(customer.getLastName())
                            .age(customer.getAge())
                            .birthDate(customer.getBirthDate())
                    .build())
        );
    }
}
