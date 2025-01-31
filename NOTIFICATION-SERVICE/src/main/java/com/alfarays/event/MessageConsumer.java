package com.alfarays.event;

import com.alfarays.model.OrderConfirmation;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static com.alfarays.event.TopicConstants.ORDER_CONFIRM_NOTIFICATION_TOPIC;

@Service
public class MessageConsumer {

    @KafkaListener(topics = ORDER_CONFIRM_NOTIFICATION_TOPIC, groupId = "group_id")
    public void consume(OrderConfirmation message){
        System.out.println("Received message: " + message);
    }

}
