package com.fernando.manantial_ms_customers.infrastructure.adapters.input.rest.models.response;

import lombok.Builder;

import java.util.List;

@Builder
public record PaginatedResponse<T> (
     List<T> content,
     int page,
     int size,
     long totalElements,
     int totalPages
){}
