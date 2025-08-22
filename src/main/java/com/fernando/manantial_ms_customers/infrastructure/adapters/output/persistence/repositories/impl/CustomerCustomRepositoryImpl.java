package com.fernando.manantial_ms_customers.infrastructure.adapters.output.persistence.repositories.impl;

import com.fernando.manantial_ms_customers.infrastructure.adapters.output.persistence.models.CustomerDocument;
import com.fernando.manantial_ms_customers.infrastructure.adapters.output.persistence.repositories.CustomerCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@RequiredArgsConstructor
public class CustomerCustomRepositoryImpl implements CustomerCustomRepository {
    private final ReactiveMongoTemplate reactiveMongoTemplate;
    @Override
    public Flux<CustomerDocument> getCustomerPaginated(int page, int size) {
        Query query = new Query()
                .with(Sort.by(Sort.Direction.DESC, "createdAt"))  // Ordenar por fecha descendente
                .skip((long) (page-1)*size)
                .limit(size);

        return reactiveMongoTemplate.find(query, CustomerDocument.class);
    }
}
