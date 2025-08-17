package com.fernando.manantial_ms_customers.domain.services;

import com.fernando.manantial_ms_customers.Utils.TestUtilCustomer;
import com.fernando.manantial_ms_customers.application.ports.input.GetCustomerUseCase;
import com.fernando.manantial_ms_customers.application.ports.output.CalculateMetricsPort;
import com.fernando.manantial_ms_customers.application.ports.output.CustomerEventPort;
import com.fernando.manantial_ms_customers.application.ports.output.CustomerPersistencePort;
import com.fernando.manantial_ms_customers.domain.exceptions.CustomerNotFoundException;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServicesTest {
    @Mock
    private CustomerPersistencePort customerPersistencePort;

    @Mock
    private List<CustomerRule> listCustomerRule;

    @Mock
    private CalculateMetricsPort calculateMetricsPort;

    @Mock
    private CustomerEventPort customerEventPort;

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
                     assertEquals(customer.getLifeExpectancy(),customer1.getLifeExpectancy());
                })
                .consumeNextWith(customer -> {
                    assertEquals(customer.getName(),customer2.getName());
                    assertEquals(customer.getLastName(),customer2.getLastName());
                    assertEquals(customer.getAge(),customer2.getAge());
                    assertEquals(customer.getBirthDate(),customer2.getBirthDate());
                    assertEquals(customer.getLifeExpectancy(),customer2.getLifeExpectancy());
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
        doNothing().when(customerEventPort).publishCustomerSaved(any(Customer.class));
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
        Mockito.verify(customerEventPort,times(1)).publishCustomerSaved(any(Customer.class));
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
        Mockito.verify(customerEventPort,times(0)).publishCustomerSaved(any(Customer.class));
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
        Mockito.verify(customerEventPort,times(0)).publishCustomerSaved(any(Customer.class));
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

    @Test
    @DisplayName("When Information Customer Is Correct Expect Customer Updated")
    void When_InformationCustomerIsCorrectExpectCustomerUpdated(){
        Customer customer1 = TestUtilCustomer.buildMockCustomer();
        Customer customerInfo = TestUtilCustomer.buildMockCustomer();
        when(customerPersistencePort.saveCustomer(any(Customer.class))).thenReturn(Mono.just(customer1));
        when(listCustomerRule.stream()).thenReturn(Stream.of(new CompareAgeWithAgeOfBirthDate()));
        when(customerPersistencePort.getCustomer(anyString())).thenReturn(Mono.just(customerInfo));
        doNothing().when(customerEventPort).publishCustomerSaved(any(Customer.class));
        Mono<Customer> customerMono= customerService.update("1", customer1);

        StepVerifier.create(customerMono)
                .expectNextMatches(customer->
                        customer.getId().equals(customer1.getId())
                                && customer.getName().equals(customer1.getName())
                                && customer.getLastName().equals(customer1.getLastName())
                                && customer.getAge().equals(customer1.getAge())
                                && customer.getBirthDate().equals(customer1.getBirthDate()))
                .verifyComplete();

        Mockito.verify(customerPersistencePort,times(1)).saveCustomer(any(Customer.class));
        Mockito.verify(customerEventPort,times(1)).publishCustomerSaved(any(Customer.class));
        Mockito.verify(customerPersistencePort,times(1)).getCustomer(anyString());
        Mockito.verify(listCustomerRule,times(1)).stream();
    }


    @Test
    @DisplayName("Expect CustomerRuleException When Rule Customer Is Not Valid2")
    void Expect_CustomerRuleException_When_RuleCustomerIsNotValid2(){
        Customer customer1 = TestUtilCustomer.buildMockCustomer();
        Customer customerInfo = TestUtilCustomer.buildMockCustomer();
        customer1.setAge(36);
        when(listCustomerRule.stream()).thenReturn(Stream.of(new CompareAgeWithAgeOfBirthDate()));
        when(customerPersistencePort.getCustomer(anyString())).thenReturn(Mono.just(customerInfo));
        Mono<Customer> customerMono= customerService.update("1",customer1);

        StepVerifier.create(customerMono)
                .expectError(CustomerRuleException.class)
                .verify();

        Mockito.verify(customerPersistencePort,times(0)).saveCustomer(any(Customer.class));
        Mockito.verify(customerEventPort,times(0)).publishCustomerSaved(any(Customer.class));
        Mockito.verify(customerPersistencePort,times(1)).getCustomer(anyString());
        Mockito.verify(listCustomerRule,times(1)).stream();
    }

    @Test
    @DisplayName("Expect RuleStrategyException When Rule Code Do Not Exists2")
    void Expect_RuleStrategyException_When_RuleCodeDoNotExists2(){
        Customer customer1 = TestUtilCustomer.buildMockCustomer();
        customer1.setAge(36);
        when(listCustomerRule.stream()).thenReturn(Stream.empty());
        Mono<Customer> customerMono= customerService.update("1",customer1);

        StepVerifier.create(customerMono)
                .expectError(RuleStrategyException.class)
                .verify();

        Mockito.verify(customerPersistencePort,times(0)).saveCustomer(any(Customer.class));
        Mockito.verify(customerEventPort,times(0)).publishCustomerSaved(any(Customer.class));
        Mockito.verify(customerPersistencePort,times(0)).getCustomer(anyString());
        Mockito.verify(listCustomerRule,times(1)).stream();
    }

    @Test
    @DisplayName("Expect CustomerNotFoundException When Customer Id Do Not Exists In Update")
    void Expect_CustomerNotFoundException_When_CustomerIdDoNotExistsInUpdate(){
        Customer customer1 = TestUtilCustomer.buildMockCustomer();
        when(customerPersistencePort.getCustomer(anyString())).thenReturn(Mono.empty());
        when(listCustomerRule.stream()).thenReturn(Stream.of(new CompareAgeWithAgeOfBirthDate()));
        Mono<Customer> customerMono= customerService.update("1", customer1);

        StepVerifier.create(customerMono)
                .expectError(CustomerNotFoundException.class)
                .verify();

        Mockito.verify(customerPersistencePort,times(0)).saveCustomer(any(Customer.class));
        Mockito.verify(customerEventPort,times(0)).publishCustomerSaved(any(Customer.class));
        Mockito.verify(customerPersistencePort,times(1)).getCustomer(anyString());
        Mockito.verify(listCustomerRule,times(1)).stream();
    }

    @Test
    @DisplayName("When CustomerId Exists Expect Delete Customer By Id")
    void When_CustomerIdExists_Expect_DeleteCustomerById(){
        Customer customer=TestUtilCustomer.buildMockCustomer();
        when(customerPersistencePort.getCustomer(anyString())).thenReturn(Mono.just(customer));
        when(customerPersistencePort.deleteCustomer(anyString())).thenReturn(Mono.empty());
        doNothing().when(customerEventPort).publishCustomerDeleted(anyString());
        Mono<Void> customerMono = customerService.delete("1");
        StepVerifier.create(customerMono)
                .verifyComplete();
        Mockito.verify(customerPersistencePort,times(1)).getCustomer(anyString());
        Mockito.verify(customerPersistencePort,times(1)).deleteCustomer(anyString());
        Mockito.verify(customerEventPort,times(1)).publishCustomerDeleted(anyString());
    }

    @Test
    @DisplayName("Expect CustomerNotFoundException When CustomerId Is Incorrect")
    void Expect_CustomerNotFoundException_When_CustomerIdIsIncorrect(){
        when(customerPersistencePort.getCustomer(anyString())).thenReturn(Mono.empty());
        Mono<Void> customerMono = customerService.delete("1");
        StepVerifier.create(customerMono)
                .expectError(CustomerNotFoundException.class)
                        .verify();
        Mockito.verify(customerPersistencePort,times(1)).getCustomer(anyString());
        Mockito.verify(customerPersistencePort,times(0)).deleteCustomer(anyString());
        Mockito.verify(customerEventPort,times(0)).publishCustomerDeleted(anyString());
    }

    @Test
    @DisplayName("When CustomerId Exists Expect Customer Information")
    void When_CustomerIdExists_Expect_CustomerInformation(){
        Customer customer=TestUtilCustomer.buildMockCustomer();
        when(customerPersistencePort.getCustomer(anyString())).thenReturn(Mono.just(customer));
        Mono<Customer> customerMono = customerService.getCustomer("1");
        StepVerifier.create(customerMono)
                .expectNextMatches(customerInfo->
                        customerInfo.getId().equals(customer.getId())
                                && customerInfo.getName().equals(customer.getName())
                                && customerInfo.getLastName().equals(customer.getLastName())
                                && customerInfo.getAge().equals(customer.getAge())
                                && customerInfo.getBirthDate().equals(customer.getBirthDate()))
                .verifyComplete();
        Mockito.verify(customerPersistencePort,times(1)).getCustomer(anyString());
    }

    @Test
    @DisplayName("When CustomerId Exists Expect Customer Information")
    void Expect_CustomerNotFoundException_When_CustomerIdDoNotExists(){
        when(customerPersistencePort.getCustomer(anyString())).thenReturn(Mono.empty());
        Mono<Customer> customerMono = customerService.getCustomer("1");
        StepVerifier.create(customerMono)
                .expectError(CustomerNotFoundException.class)
                .verify();
        Mockito.verify(customerPersistencePort,times(1)).getCustomer(anyString());
    }


}
