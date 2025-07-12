package com.fernando.manantial_ms_customers.infrastructure.adapters.input.rest.mappers.impl;

import com.fernando.manantial_ms_customers.domain.models.Customer;
import com.fernando.manantial_ms_customers.infrastructure.adapters.input.rest.mappers.CustomerRestMapper;
import com.fernando.manantial_ms_customers.infrastructure.adapters.input.rest.models.response.CustomerResponse;
import reactor.core.publisher.Flux;

public class CustomerRestMapperImpl implements CustomerRestMapper {
    @Override
    public Flux<CustomerResponse> customerFluxToCustomerResponseFlux(Flux<Customer> customerFlux) {
        return customerFlux.flatMap(customer->{
            return Flux.just(
                    CustomerResponse.builder()
                            .id(customer.getId())
                            .name(customer.getName())
                            .age(customer.getAge())
                            .birthDate(customer.getBirthDate())
                            .build()
            );
        });
    }
}
