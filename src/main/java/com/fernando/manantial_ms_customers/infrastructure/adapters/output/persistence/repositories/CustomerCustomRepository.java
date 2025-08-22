package com.fernando.manantial_ms_customers.infrastructure.adapters.output.persistence.repositories;

import com.fernando.manantial_ms_customers.infrastructure.adapters.output.persistence.models.CustomerDocument;
import reactor.core.publisher.Flux;

public interface CustomerCustomRepository {
    Flux<CustomerDocument> getCustomerPaginated(int page, int size);
}
