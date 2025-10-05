package com.fernando.manantial_ms_customers.application.ports.input;

import reactor.core.publisher.Mono;

public interface GetCustomerFileUseCase {
    Mono<byte[]> getFile(String path);
}
