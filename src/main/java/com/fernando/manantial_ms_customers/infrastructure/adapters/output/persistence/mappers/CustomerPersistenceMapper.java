package com.fernando.manantial_ms_customers.infrastructure.adapters.output.persistence.mappers;

import com.fernando.manantial_ms_customers.domain.models.Customer;
import com.fernando.manantial_ms_customers.infrastructure.adapters.output.persistence.models.CustomerDocument;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerPersistenceMapper {
    Flux<Customer> customerDocumenFluxtoToCustomerFlux(Flux<CustomerDocument> customerDocumentFlux);
    CustomerDocument customerToCustomerDocument(Customer customer);
    Mono<Customer> customerDocumentMonoToCustomerMono(Mono<CustomerDocument> customerDocumentMono);
}
