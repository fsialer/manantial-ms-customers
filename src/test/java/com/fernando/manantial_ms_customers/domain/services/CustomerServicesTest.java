package com.fernando.manantial_ms_customers.domain.services;

import com.fernando.manantial_ms_customers.Utils.TestUtilCustomer;
import com.fernando.manantial_ms_customers.application.ports.input.GetCustomersUseCase;
import com.fernando.manantial_ms_customers.application.ports.input.SaveCustomerUseCase;
import com.fernando.manantial_ms_customers.application.ports.output.CustomerPersistencePort;
import com.fernando.manantial_ms_customers.domain.models.Customer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServicesTest {
    @Mock
    private CustomerPersistencePort customerPersistencePort;

    @InjectMocks
    private CustomerServices customerServices;

    @Test
    @DisplayName("When Information Of CCustomer Is Correct Expect Information Customer Saved")
    void When_InformationOfCCustomerIsCorrect_Expect_InformationCustomerSaved(){
        Customer customer1 = TestUtilCustomer.buildMockCustomer();
        Customer customer2 = TestUtilCustomer.buildMockCustomer2();
        Flux<Customer> customers=Flux.just(customer1,customer2);
        when(customerPersistencePort.getCustomers()).thenReturn(customers);
        Flux<Customer> customerFlux = customerServices.getCustomers();
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
}
