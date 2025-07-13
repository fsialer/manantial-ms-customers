package com.fernando.manantial_ms_customers.infrastructure.adapters.input.rest.models.response;

import com.fernando.manantial_ms_customers.domain.enums.ErrorType;
import lombok.Builder;

import java.util.List;

@Builder
public record ErrorResponse(String code, ErrorType type, String message, List<String> details, String timestamp) {
}
