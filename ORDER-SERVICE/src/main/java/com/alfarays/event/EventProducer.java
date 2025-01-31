package com.alfarays.event;

import com.alfarays.order.model.OrderConfirmation;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@Service
public class EventProducer {

    private static final Logger logger = LoggerFactory.getLogger(EventProducer.class);
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public EventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topic) {
        var message = OrderConfirmation
                .builder()
                .confirmationNumber("CONF12345")
                .orderId("ORD67890")
                .produceName("Fresh Apples")
                .totalSpending(29.99)
                .orderDate(LocalDateTime.now())
                .orderBy("John Doe")
                .confirmationMessage("Your order has been successfully confirmed!")
                .build();

        logger.info("Sending message to topic {}: {}", topic, message);

        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, message);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                logger.info("Message sent successfully to topic {} at offset {}", topic, result.getRecordMetadata().offset());
            } else {
                logger.error("Failed to send message to topic {}", topic, ex);
            }
        });
    }
}
