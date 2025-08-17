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

    @Test
    @DisplayName("When Customer was Saved Send an event Expect Void")
    void When_CustomerWasSavedSendAnEvent_Expect_Void(){
        // Arrange
        KafkaTemplate<String, Object> kafkaTemplate = mock(KafkaTemplate.class);
        CustomerEventAdapter customerEventAdapter = new CustomerEventAdapter(kafkaTemplate);
        Customer customer = TestUtilCustomer.buildMockCustomer();

        ProducerRecord<String, Object> producerRecord = new ProducerRecord<>("customer-topic", customer);
        RecordMetadata recordMetadata = new RecordMetadata(new TopicPartition("customer-topic", 0), 0, 123L, System.currentTimeMillis(), 0L, 0, 0);

        SendResult<String, Object> sendResult = new SendResult<>(producerRecord, recordMetadata);

        CompletableFuture<SendResult<String, Object>> future = CompletableFuture.completedFuture(sendResult);
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
        KafkaTemplate<String, Object> kafkaTemplate = mock(KafkaTemplate.class);
        CustomerEventAdapter customerEventAdapter = new CustomerEventAdapter(kafkaTemplate);
        Customer customer = TestUtilCustomer.buildMockCustomer();
        CompletableFuture<SendResult<String, Object>> future = new CompletableFuture<>();
        future.completeExceptionally(new RuntimeException("Kafka error"));
        when(kafkaTemplate.send(anyString(), any(Customer.class))).thenReturn(future);

        // Act
        customerEventAdapter.publishCustomerSaved(customer);

        // Assert
        Mockito.verify(kafkaTemplate,times(1)).send(anyString(), any(Customer.class));
    }

    @Test
    @DisplayName("When Customer was Deleted Send an event Expect Void")
    void When_CustomerWasDeletedSendAnEvent_Expect_Void(){
        KafkaTemplate<String, Object> kafkaTemplate = mock(KafkaTemplate.class);
        CustomerEventAdapter customerEventAdapter = new CustomerEventAdapter(kafkaTemplate);
        ProducerRecord<String, Object> producerRecord = new ProducerRecord<>("delete-customer-topic", "1");
        RecordMetadata recordMetadata = new RecordMetadata(new TopicPartition("delete-customer-topic", 0), 0, 123L, System.currentTimeMillis(), 0L, 0, 0);

        SendResult<String, Object> sendResult = new SendResult<>(producerRecord, recordMetadata);

        CompletableFuture<SendResult<String, Object>> future = CompletableFuture.completedFuture(sendResult);
        when(kafkaTemplate.send(anyString(), anyString())).thenReturn(future);

        // Act
        customerEventAdapter.publishCustomerDeleted("1");

        // Assert
        Mockito.verify(kafkaTemplate,times(1)).send(anyString(), anyString());
    }

    @Test
    @DisplayName("Expect RuntimeException When There are fails in sending Message To The Delete A Customer")
    void Expect_RuntimeException_When_ThereAreFailsInSendingMessageToTheDeleteACustomer() {
        // Arrange
        KafkaTemplate<String, Object> kafkaTemplate = mock(KafkaTemplate.class);
        CustomerEventAdapter customerEventAdapter = new CustomerEventAdapter(kafkaTemplate);
        CompletableFuture<SendResult<String, Object>> future = new CompletableFuture<>();
        future.completeExceptionally(new RuntimeException("Kafka error"));
        when(kafkaTemplate.send(anyString(), anyString())).thenReturn(future);

        // Act
        customerEventAdapter.publishCustomerDeleted("1");

        // Assert
        Mockito.verify(kafkaTemplate,times(1)).send(anyString(), anyString());
    }

}
