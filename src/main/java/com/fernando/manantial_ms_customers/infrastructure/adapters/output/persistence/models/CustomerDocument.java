package com.fernando.manantial_ms_customers.infrastructure.adapters.output.persistence.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
    private LocalDateTime createdAt;
    private String pathFile;
}
