package com.fernando.manantial_ms_customers.domain.services;

import com.fernando.manantial_ms_customers.Utils.TestUtilCustomer;
import com.fernando.manantial_ms_customers.application.ports.output.CalculateMetricsPort;
import com.fernando.manantial_ms_customers.application.ports.output.CustomerPersistencePort;
import com.fernando.manantial_ms_customers.domain.exceptions.CustomerRuleException;
import com.fernando.manantial_ms_customers.domain.exceptions.RuleStrategyException;
import com.fernando.manantial_ms_customers.domain.models.Customer;
import com.fernando.manantial_ms_customers.domain.models.Metric;
import com.fernando.manantial_ms_customers.domain.strategy.CompareAgeWithAgeOfBirthDate;
import com.fernando.manantial_ms_customers.domain.strategy.CustomerRule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServicesTest {
    @Mock
    private CustomerPersistencePort customerPersistencePort;

    @Mock
    private List<CustomerRule> listCustomerRule;

    @Mock
    private CalculateMetricsPort calculateMetricsPort;

    @InjectMocks
    private CustomerService customerService;

    @Test
    @DisplayName("When Information Of Customer Is Correct Expect Information Customer Saved")
    void When_InformationOfCustomerIsCorrect_Expect_InformationCustomerSaved(){
        Customer customer1 = TestUtilCustomer.buildMockCustomer();
        Customer customer2 = TestUtilCustomer.buildMockCustomer2();
        Flux<Customer> customers=Flux.just(customer1,customer2);
        when(customerPersistencePort.getCustomers()).thenReturn(customers);
        Flux<Customer> customerFlux = customerService.getCustomers();
        StepVerifier.create(customerFlux)
                .consumeNextWith(customer->{
                     assertEquals(customer.getName(),customer1.getName());
                     assertEquals(customer.getLastName(),customer1.getLastName());
                     assertEquals(customer.getAge(),customer1.getAge());
                     assertEquals(customer.getBirthDate(),customer1.getBirthDate());
                })
                .consumeNextWith(customer -> {
                    assertEquals(customer.getName(),customer2.getName());
                    assertEquals(customer.getLastName(),customer2.getLastName());
                    assertEquals(customer.getAge(),customer2.getAge());
                    assertEquals(customer.getBirthDate(),customer2.getBirthDate());
                })
                .verifyComplete();
        Mockito.verify(customerPersistencePort,times(1)).getCustomers();
    }

    @Test
    @DisplayName("When Information Customer Is Correct Expect Customer Saved")
    void When_InformationCustomerIsCorrectExpectCustomerSaved(){
        Customer customer1 = TestUtilCustomer.buildMockCustomer();
        when(customerPersistencePort.saveCustomer(any(Customer.class))).thenReturn(Mono.just(customer1));
        when(listCustomerRule.stream()).thenReturn(Stream.of(new CompareAgeWithAgeOfBirthDate()));
        Mono<Customer> customerMono= customerService.save(customer1);

        StepVerifier.create(customerMono)
                .expectNextMatches(customer->
                        customer.getId().equals(customer1.getId())
                        && customer.getName().equals(customer1.getName())
                        && customer.getLastName().equals(customer1.getLastName())
                        && customer.getAge().equals(customer1.getAge())
                        && customer.getBirthDate().equals(customer1.getBirthDate()))
                .verifyComplete();

        Mockito.verify(customerPersistencePort,times(1)).saveCustomer(any(Customer.class));
    }


    @Test
    @DisplayName("Expect CustomerRuleException When Rule Customer Is Not Valid")
    void Expect_CustomerRuleException_When_RuleCustomerIsNotValid(){
        Customer customer1 = TestUtilCustomer.buildMockCustomer();
        customer1.setAge(36);
        when(customerPersistencePort.saveCustomer(any(Customer.class))).thenReturn(Mono.just(customer1));
        when(listCustomerRule.stream()).thenReturn(Stream.of(new CompareAgeWithAgeOfBirthDate()));
        Mono<Customer> customerMono= customerService.save(customer1);

        StepVerifier.create(customerMono)
                .expectError(CustomerRuleException.class)
                .verify();

        Mockito.verify(customerPersistencePort,times(1)).saveCustomer(any(Customer.class));
    }

    @Test
    @DisplayName("Expect RuleStrategyException When Rule Code Do Not Exists")
    void Expect_RuleStrategyException_When_RuleCodeDoNotExists(){
        Customer customer1 = TestUtilCustomer.buildMockCustomer();
        customer1.setAge(36);
        when(listCustomerRule.stream()).thenReturn(Stream.empty());
        Mono<Customer> customerMono= customerService.save(customer1);

        StepVerifier.create(customerMono)
                .expectError(RuleStrategyException.class)
                .verify();

        Mockito.verify(customerPersistencePort,times(0)).saveCustomer(any(Customer.class));
    }

    @Test
    @DisplayName("When Have Customer Available Expect CalculateMatrics")
    void When_HaveCustomerAvailable_Expect_CalculateMatrics(){
        Customer customer1 = TestUtilCustomer.buildMockCustomer();
        Customer customer2 = TestUtilCustomer.buildMockCustomer2();
        Customer customer3 = TestUtilCustomer.buildMockCustomer3();
        Flux<Customer> customers=Flux.just(customer1,customer2,customer3);
        when(customerPersistencePort.getCustomers()).thenReturn(customers);
        Mono<Metric> metricMono=customerService.getMetrics();
        when(calculateMetricsPort.calculateAverage(anyList())).thenReturn(31.0);
        when(calculateMetricsPort.calculateStandardDeviation(anyList(),anyDouble())).thenReturn(4.666666666666667);

        StepVerifier.create(metricMono)
                .consumeNextWith(metric->{
                    assertEquals(31.0, metric.getAverage());
                    assertEquals(4.666666666666667, metric.getStandardDeviation());
                })
                .verifyComplete();
        Mockito.verify(customerPersistencePort,times(1)).getCustomers();
        Mockito.verify(calculateMetricsPort,times(1)).calculateAverage(anyList());
        Mockito.verify(calculateMetricsPort,times(1)).calculateStandardDeviation(anyList(),anyDouble());
    }
}
