package com.fernando.manantial_ms_customers.infrastructure.adapters.output.event;

import com.fernando.manantial_ms_customers.application.ports.output.CustomerEventPort;
import com.fernando.manantial_ms_customers.domain.models.Customer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomerEventAdapter implements CustomerEventPort {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void publishCustomerSaved(Customer customer) {
        this.sendMessage("customer-topic",customer);
    }

    @Override
    public void publishCustomerDeleted(String path) {
        this.sendMessage("delete-customer-topic",path);
    }

    private <T> void sendMessage(String topic, T message) {
        kafkaTemplate.send(topic, message).whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("❌ Error sending message to topic '{}': {}", topic, ex.getMessage(), ex);
            } else {
                log.info("✅ Message sent to topic '{}': {}", topic, result.getProducerRecord().value());
                log.info("Partition: {}, Offset: {}", result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
            }
        });
    }
}
