package com.fernando.manantial_ms_customers.infrastructure.adapters.output.calculate;

import com.fernando.manantial_ms_customers.application.ports.output.CalculateMetricsPort;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CalculateMetricsAdapter implements CalculateMetricsPort {

    @Override
    public Double calculateAverage(List<Integer> ages) {
        return ages.stream()
                .mapToDouble(Integer::doubleValue).average().orElse(0);
    }

    @Override
    public Double calculateStandardDeviation(List<Integer> ages, Double average) {
        return ages.stream()
                .mapToDouble(age-> Math.pow((age-average),2)).average().orElse(0);
    }
}
