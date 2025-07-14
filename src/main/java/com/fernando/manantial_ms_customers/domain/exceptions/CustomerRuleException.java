package com.fernando.manantial_ms_customers.domain.exceptions;

public class CustomerRuleException extends RuntimeException{
    public CustomerRuleException(String message){
        super(message);
    }
}
