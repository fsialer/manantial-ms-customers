package com.fernando.manantial_ms_customers.infrastructure.adapters.input.rest.models.response;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record CustomerResponse(String id, String name, String lastName, Integer age, LocalDate birthDate) {}
