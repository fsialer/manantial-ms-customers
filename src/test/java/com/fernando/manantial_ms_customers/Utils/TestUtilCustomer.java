package com.fernando.manantial_ms_customers.Utils;

import com.fernando.manantial_ms_customers.domain.models.Customer;
import com.fernando.manantial_ms_customers.domain.models.Metric;
import com.fernando.manantial_ms_customers.infrastructure.adapters.input.rest.models.request.CustomerRequest;
import com.fernando.manantial_ms_customers.infrastructure.adapters.input.rest.models.response.CustomerResponse;
import com.fernando.manantial_ms_customers.infrastructure.adapters.input.rest.models.response.MetricResponse;
import com.fernando.manantial_ms_customers.infrastructure.adapters.output.persistence.models.CustomerDocument;

import java.time.LocalDate;

public class TestUtilCustomer {
    public static Customer buildMockCustomer(){
        return Customer.builder()
                .id("541dikjd454dsplew")
                .name("John")
                .lastName("Doe")
                .age(34)
                .birthDate(LocalDate.of(1991,5,14))
                .build();
    }

    public static Customer buildMockCustomer2(){
        return Customer.builder()
                .id("541dikjdysd854dsdsd")
                .name("Jane")
                .lastName("Doe")
                .age(29)
                .birthDate(LocalDate.of(1994,5,14))
                .build();
    }

    public static Customer buildMockCustomer3(){
        return Customer.builder()
                .id("541dikjdysd854deopod")
                .name("Fernando")
                .lastName("Aguilar")
                .age(30)
                .birthDate(LocalDate.of(1995,5,14))
                .build();
    }

    public static CustomerDocument buildMockCustomerDocument(){
        return CustomerDocument.builder()
                .id("541dikjd454dsplew")
                .name("John")
                .lastName("Doe")
                .age(34)
                .birthDate(LocalDate.of(1991,5,14))
                .build();
    }

    public static CustomerDocument buildMockCustomerDocument2(){
        return CustomerDocument.builder()
                .id("541dikjdysd854dsdsd")
                .name("Jane")
                .lastName("Doe")
                .age(30)
                .birthDate(LocalDate.of(1995,5,14))
                .build();
    }

    public static CustomerResponse buildMockCustomerResponse(){
        return CustomerResponse.builder()
                .id("541dikjd454dsplew")
                .name("John")
                .lastName("Doe")
                .age(34)
                .birthDate(LocalDate.of(1991,5,14))
                .build();
    }

    public static CustomerResponse buildMockCustomerResponse2(){
        return CustomerResponse.builder()
                .id("541dikjdysd854dsdsd")
                .name("Jane")
                .lastName("Doe")
                .age(30)
                .birthDate(LocalDate.of(1995,5,14))
                .build();
    }

    public static CustomerRequest buildMockCustomerRequest(){
        return CustomerRequest.builder()
                .name("John")
                .lastName("Doe")
                .age(35)
                .birthDate(LocalDate.of(1991,5,14))
                .build();
    }


    public static Metric buildMockMetric(){
        return Metric.builder()
                .average(31.0)
                .standardDeviation(4.666666666666667)
                .build();
    }

    public static MetricResponse buildMockMetricResponse(){
        return MetricResponse.builder()
                .average(31.0)
                .standardDeviation(4.666666666666667)
                .build();
    }
}
