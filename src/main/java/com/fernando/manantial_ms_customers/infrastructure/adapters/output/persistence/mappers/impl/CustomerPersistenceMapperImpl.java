package com.fernando.manantial_ms_customers.infrastructure.adapters.output.persistence.mappers.impl;

import com.fernando.manantial_ms_customers.domain.models.Customer;
import com.fernando.manantial_ms_customers.infrastructure.adapters.output.persistence.mappers.CustomerPersistenceMapper;
import com.fernando.manantial_ms_customers.infrastructure.adapters.output.persistence.models.CustomerDocument;
import reactor.core.publisher.Flux;

public class CustomerPersistenceMapperImpl implements CustomerPersistenceMapper {
    public Flux<Customer> customerDocumenFluxtoToCustomerFlux(Flux<CustomerDocument> customerDocumentFlux){
        return customerDocumentFlux.flatMap(customer->{
            return Flux.just(
                    Customer.builder()
                            .id(customer.getId())
                            .name(customer.getName())
                            .lastName(customer.getLastName())
                            .age(customer.getAge())
                            .birthDate(customer.getBirthDate())
                            .build()
            );
        });

    }
}
