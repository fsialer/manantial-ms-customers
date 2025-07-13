package com.fernando.manantial_ms_customers.infrastructure.adapters.input.rest.models.response;

import lombok.Builder;

@Builder
public record MetricResponse(Double average, Double standardDeviation) {
}
