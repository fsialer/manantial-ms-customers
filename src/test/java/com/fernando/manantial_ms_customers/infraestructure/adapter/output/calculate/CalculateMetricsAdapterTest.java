package com.fernando.manantial_ms_customers.infraestructure.adapter.output.calculate;

import com.fernando.manantial_ms_customers.infrastructure.adapters.output.calculate.CalculateMetricsAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class CalculateMetricsAdapterTest {
    @InjectMocks
    private CalculateMetricsAdapter calculateMetricsAdapter;

    @Test
    @DisplayName("When List Ages Is Available Expect Calculate Average")
    void When_ListAgesIsAvailable_Expect_CalculateAverage(){
        List<Integer> ages= List.of(34,30,29);
        Double average = calculateMetricsAdapter.calculateAverage(ages);
        assertEquals(31,average);
    }

    @Test
    @DisplayName("When List Ages And Average Are Available Expect Calculate Standard Deviation")
    void When_ListAgesAndAverageAreAvailable_Expect_CalculateStandardDeviation(){
        List<Integer> ages= List.of(34,30,29);
        Double average = calculateMetricsAdapter.calculateAverage(ages);
        Double standardDeviation = calculateMetricsAdapter.calculateStandardDeviation(ages,average);
        assertEquals(4.666666666666667,standardDeviation);
    }
}
