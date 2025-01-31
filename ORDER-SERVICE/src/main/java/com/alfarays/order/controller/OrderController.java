package com.alfarays.order.controller;

import com.alfarays.event.EventProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.alfarays.event.MessageConstants.ORDER_CONFIRM_NOTIFICATION_TOPIC;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final EventProducer eventProducer;

    @GetMapping("/send")
    public void sendMessage() {
        eventProducer.sendMessage(ORDER_CONFIRM_NOTIFICATION_TOPIC);
    }

}
