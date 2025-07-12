package com.fernando.manantial_ms_customers.domain.models;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    private String id;
    private String name;
    private String lastName;
    private Integer age;
    private LocalDate birthDate;
}
