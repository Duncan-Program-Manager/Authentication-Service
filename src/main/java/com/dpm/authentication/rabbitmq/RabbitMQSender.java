package com.dpm.authentication.rabbitmq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQSender {
    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange}")
    private String exchange;
    @Value("${rabbitmq.routingkey1}")
    private String routingKey;

    public void send(Message message) {
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }
}
