package com.fernando.manantial_ms_customers.infrastructure.adapters.input.rest.mappers;

import com.fernando.manantial_ms_customers.domain.models.Customer;
import com.fernando.manantial_ms_customers.domain.models.Metric;
import com.fernando.manantial_ms_customers.infrastructure.adapters.input.rest.models.request.CustomerRequest;
import com.fernando.manantial_ms_customers.infrastructure.adapters.input.rest.models.response.CustomerResponse;
import com.fernando.manantial_ms_customers.infrastructure.adapters.input.rest.models.response.MetricResponse;
import org.yaml.snakeyaml.util.Tuple;
import reactor.core.publisher.Flux;

import java.util.List;

public interface CustomerRestMapper {
    Flux<CustomerResponse> customerFluxToCustomerResponseFlux(Flux<Customer> customerFlux);
    Customer customerRequestToCustomer(CustomerRequest customerRequest);
    CustomerResponse customerToCustomerResponse(Customer customer);
    MetricResponse metricToMetricResponse(Metric metric);
    List<CustomerResponse>  customerListToCustomerResponseList(List<Customer> list);
}
