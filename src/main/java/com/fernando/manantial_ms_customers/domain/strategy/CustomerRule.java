package com.fernando.manantial_ms_customers.domain.strategy;

import com.fernando.manantial_ms_customers.domain.models.Customer;
import reactor.core.publisher.Mono;

public interface CustomerRule {
    Mono<Void> validateRule(Customer customer);
    boolean isApplicable(String codeValidation);
}
