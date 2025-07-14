package com.fernando.manantial_ms_customers.infrastructure.adapters.output.persistence.repositories;

import com.fernando.manantial_ms_customers.infrastructure.adapters.output.persistence.models.CustomerDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CustomerRepository extends ReactiveMongoRepository<CustomerDocument, String> {
}
