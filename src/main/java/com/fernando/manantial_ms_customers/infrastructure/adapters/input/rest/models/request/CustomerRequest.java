package com.fernando.manantial_ms_customers.infrastructure.adapters.input.rest.models.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRequest {
    @NotBlank(message = "Field name cannot be empty or null")
    private String name;
    @NotBlank(message = "Field lastName cannot be empty or null")
    private String lastName;
    @NotNull(message = "Field age cannot be null")
    @Min(value=18, message = "Field age cannot be to 18")
    private Integer age;
    @NotNull(message = "Field birthDate cannot be null")
    private LocalDate birthDate;
}
