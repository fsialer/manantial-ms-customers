package com.fernando.manantial_ms_customers.infrastructure.adapters.output.event;

import com.fernando.manantial_ms_customers.application.ports.output.CustomerEventPort;
import com.fernando.manantial_ms_customers.domain.models.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerEventAdapter implements CustomerEventPort {
    private final KafkaTemplate<String, Customer> kafkaTemplate;

    @Override
    public void publishCustomerSaved(Customer customer) {
        kafkaTemplate.send("customer-topic",customer);
    }
}
