package com.fernando.manantial_ms_customers.infrastructure.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCatalog {
    CUSTOMER_INTERNAL_SERVER_ERROR("CUSTOMER_000", "Internal server error."),
    CUSTOMER_BAD_PARAMETER("CUSTOMER_001", "Invalid parameters for creation customer"),
    CUSTOMER_RULE("CUSTOMER_002","A rule is not valid"),
    CUSTOMER_RULE_STRATEGY("CUSTOMER_003","A code rule is not valid");
    private final String code;
    private final String message;
}
