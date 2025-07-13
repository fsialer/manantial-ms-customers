package com.fernando.manantial_ms_customers.infrastructure.adapters.input.rest.mappers.impl;

import com.fernando.manantial_ms_customers.domain.models.Customer;
import com.fernando.manantial_ms_customers.domain.models.Metric;
import com.fernando.manantial_ms_customers.infrastructure.adapters.input.rest.mappers.CustomerRestMapper;
import com.fernando.manantial_ms_customers.infrastructure.adapters.input.rest.models.request.CustomerRequest;
import com.fernando.manantial_ms_customers.infrastructure.adapters.input.rest.models.response.CustomerResponse;
import com.fernando.manantial_ms_customers.infrastructure.adapters.input.rest.models.response.MetricResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
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

    @Override
    public Customer customerRequestToCustomer(CustomerRequest customerRequest) {
        return Customer.builder()
                .name(customerRequest.getName())
                .lastName(customerRequest.getLastName())
                .age(customerRequest.getAge())
                .birthDate(customerRequest.getBirthDate())
                .build();
    }

    @Override
    public CustomerResponse customerToCustomerResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .lastName(customer.getLastName())
                .age(customer.getAge())
                .birthDate(customer.getBirthDate())
                .build();
    }

    @Override
    public MetricResponse metricToMetricResponse(Metric metric) {
        return MetricResponse.builder()
                .average(metric.getAverage())
                .standardDeviation(metric.getStandardDeviation())
                .build();
    }
}
