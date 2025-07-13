package com.fernando.manantial_ms_customers.infrastructure.adapters.input.rest;


import com.fernando.manantial_ms_customers.domain.exceptions.CustomerRuleException;
import com.fernando.manantial_ms_customers.domain.exceptions.RuleStrategyException;
import com.fernando.manantial_ms_customers.infrastructure.adapters.input.rest.models.response.ErrorResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static com.fernando.manantial_ms_customers.domain.enums.ErrorType.FUNCTIONAL;
import static com.fernando.manantial_ms_customers.domain.enums.ErrorType.SYSTEM;
import static com.fernando.manantial_ms_customers.infrastructure.utils.ErrorCatalog.*;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CustomerRuleException.class)
    public Mono<ErrorResponse> handleCustomerRuleException(
            CustomerRuleException e) {

        return Mono.just(ErrorResponse.builder()
                .code(CUSTOMER_RULE.getCode())
                .type(FUNCTIONAL)
                .message(CUSTOMER_RULE.getMessage())
                .details(List.of(e.getMessage()))
                .timestamp(LocalDate.now().toString())
                .build());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RuleStrategyException.class)
    public Mono<ErrorResponse> handleRuleStrategyException(
            RuleStrategyException e) {

        return Mono.just(ErrorResponse.builder()
                .code(CUSTOMER_RULE_STRATEGY.getCode())
                .type(FUNCTIONAL)
                .message(CUSTOMER_RULE_STRATEGY.getMessage())
                .details(List.of(e.getMessage()))
                .timestamp(LocalDate.now().toString())
                .build());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ErrorResponse> handleWebExchangeBindException(
            WebExchangeBindException e) {
        BindingResult bindingResult = e.getBindingResult();
        return Mono.just(ErrorResponse.builder()
                .code(CUSTOMER_BAD_PARAMETER.getCode())
                .type(FUNCTIONAL)
                .message(CUSTOMER_BAD_PARAMETER.getMessage())
                .details(bindingResult.getFieldErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .toList())
                .timestamp(LocalDate.now().toString())
                .build());
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Mono<ErrorResponse> handleException(Exception e) {
        return Mono.just(ErrorResponse.builder()
                .code(CUSTOMER_INTERNAL_SERVER_ERROR.getCode())
                .type(SYSTEM)
                .message(CUSTOMER_INTERNAL_SERVER_ERROR.getMessage())
                .details(Collections.singletonList(e.getMessage()))
                .timestamp(LocalDate.now().toString())
                .build());
    }
}
