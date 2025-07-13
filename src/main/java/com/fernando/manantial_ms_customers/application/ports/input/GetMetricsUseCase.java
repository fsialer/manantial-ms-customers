package com.fernando.manantial_ms_customers.application.ports.input;

import com.fernando.manantial_ms_customers.domain.models.Metric;
import reactor.core.publisher.Mono;

public interface GetMetricsUseCase {
    Mono<Metric> getMetrics();
}
