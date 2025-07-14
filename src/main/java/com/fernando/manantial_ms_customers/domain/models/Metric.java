package com.fernando.manantial_ms_customers.domain.models;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Metric {
    private Double average;
    private Double standardDeviation;
}
