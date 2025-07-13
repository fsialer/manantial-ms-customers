package com.fernando.manantial_ms_customers.application.ports.output;

import java.util.List;

public interface CalculateMetricsPort {
    Double calculateAverage(List<Integer> ages);
    Double calculateStandardDeviation(List<Integer> ages, Double average);
}
