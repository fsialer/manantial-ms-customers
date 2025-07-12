package com.fernando.manantial_ms_customers.infrastructure.adapters.output.persistence.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "customers")
@Builder
public class CustomerDocument {
    @Id
    private String id;
    private String name;
    private String lastName;
    private Integer age;
    private LocalDate birthDate;
}
