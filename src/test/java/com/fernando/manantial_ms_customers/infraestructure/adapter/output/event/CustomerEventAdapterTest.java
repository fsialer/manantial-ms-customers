package com.fernando.manantial_ms_customers.infraestructure.adapter.output.event;

import com.fernando.manantial_ms_customers.Utils.TestUtilCustomer;
import com.fernando.manantial_ms_customers.domain.models.Customer;
import com.fernando.manantial_ms_customers.infrastructure.adapters.output.event.CustomerEventAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerEventAdapterTest {
    @Mock
    private KafkaTemplate<String, Customer> kafkaTemplate;

    @InjectMocks
    private CustomerEventAdapter customerEventAdapter;

    @Test
    @DisplayName("When Customer was Saved Send an event Expect Void")
    void When_CustomerWasSavedSendAnEvent_Expect_Void(){
        Customer customer= TestUtilCustomer.buildMockCustomer();
        customerEventAdapter.publishCustomerSaved(customer);
        Mockito.verify(kafkaTemplate,times(1)).send(anyString(),any(Customer.class));
    }

}
