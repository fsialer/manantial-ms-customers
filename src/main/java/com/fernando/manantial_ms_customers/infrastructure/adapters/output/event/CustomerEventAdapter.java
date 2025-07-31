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
        kafkaTemplate.send("customer-topic",customer).whenComplete((result,ex)->{
            if(ex !=null){
                log.error("Error, send message: {}", ex.getMessage());
            }
            log.info("✅ Message sended : {}", result.getProducerRecord().value());
            log.info("Partition {}, Offset {}",result.getRecordMetadata().partition(),result.getRecordMetadata().offset());
        });
    }
}
