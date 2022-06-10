package com.dpm.authentication.rabbitmq;

import com.dpm.authentication.service.AuthService;
import com.dpm.authentication.service.UserService;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RabbitMQReciever implements RabbitListenerConfigurer {

    @Autowired
    private UserService userService;

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar rabbitListenerEndpointRegistrar) {
    }
    @RabbitListener(queues = "${rabbitmq.queue2}")
    public void receivedMessage(Message message) {
        JSONParser parser = new JSONParser();
        JSONObject json = null;
        try {
            json = (JSONObject) parser.parse(new String(message.getBody()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(json);
        if(json.get("method").equals("Delete User"))
        {
            JSONObject userInfo = (JSONObject) json.get("data");
            userService.deleteUser(userInfo.get("email").toString());
        }
    }
}
