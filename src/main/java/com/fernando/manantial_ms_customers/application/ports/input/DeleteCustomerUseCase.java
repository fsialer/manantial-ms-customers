package com.fernando.manantial_ms_customers.application.ports.input;

import reactor.core.publisher.Mono;

public interface DeleteCustomerUseCase {
    Mono<Void> delete(String id);
}
