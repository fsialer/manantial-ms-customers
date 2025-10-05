package com.fernando.manantial_ms_customers.domain.exceptions;

public class PathNotFoundException extends RuntimeException{
    public PathNotFoundException(String message){
        super(message);
    }
}
