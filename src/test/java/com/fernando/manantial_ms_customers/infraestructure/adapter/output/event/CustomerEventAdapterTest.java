package com.fernando.manantial_ms_customers.infraestructure.adapter.output.event;

import com.fernando.manantial_ms_customers.Utils.TestUtilCustomer;
import com.fernando.manantial_ms_customers.domain.models.Customer;
import com.fernando.manantial_ms_customers.infrastructure.adapters.output.event.CustomerEventAdapter;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;

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
        // Arrange
        Customer customer = TestUtilCustomer.buildMockCustomer();

        ProducerRecord<String, Customer> producerRecord = new ProducerRecord<>("customer-topic", customer);
        RecordMetadata recordMetadata = new RecordMetadata(new TopicPartition("customer-topic", 0), 0, 123L, System.currentTimeMillis(), 0L, 0, 0);

        SendResult<String, Customer> sendResult = new SendResult<>(producerRecord, recordMetadata);

        CompletableFuture<SendResult<String, Customer>> future = CompletableFuture.completedFuture(sendResult);
        when(kafkaTemplate.send(anyString(), any(Customer.class))).thenReturn(future);

        // Act
        customerEventAdapter.publishCustomerSaved(customer);

        // Assert
        Mockito.verify(kafkaTemplate,times(1)).send(anyString(), any(Customer.class));
    }

    @Test
    @DisplayName("Expect RuntimeException When There are fails in sending Message")
    void Expect_RuntimeException_When_ThereAreFailsInSendingMessage() {
        // Arrange
        Customer customer = TestUtilCustomer.buildMockCustomer();
        CompletableFuture<SendResult<String, Customer>> future = new CompletableFuture<>();
        future.completeExceptionally(new RuntimeException("Kafka error"));
        when(kafkaTemplate.send(anyString(), any(Customer.class))).thenReturn(future);

        // Act
        customerEventAdapter.publishCustomerSaved(customer);

        // Assert
        Mockito.verify(kafkaTemplate,times(1)).send(anyString(), any(Customer.class));
    }

}
