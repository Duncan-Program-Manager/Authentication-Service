package com.dpm.authentication.service;

import com.dpm.authentication.datamodels.User;
import com.dpm.authentication.dto.UserInfoDTO;
import com.dpm.authentication.logic.PasswordHasher;
import com.dpm.authentication.rabbitmq.RabbitMQSender;
import com.dpm.authentication.repository.UserRepository;
import org.json.simple.JSONObject;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.rmi.AlreadyBoundException;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    private UserRepository userRepository;
    private PasswordHasher hasher;
    private RabbitMQSender rabbitMQSender;

    @Autowired
    public AuthService(UserRepository userRepository, PasswordHasher hasher, RabbitMQSender rabbitMQSender)
    {
        this.userRepository = userRepository;
        this.hasher = hasher;
        this.rabbitMQSender = rabbitMQSender;
    }

    public String loginUser(String email, String password)
    {
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isEmpty())
        {
            return null;
        }
        if(hasher.getEncoder().matches(password, user.get().getPassword()))
        {
            return hasher.createJWT(user.get());
        }
        return null;
    }

    public void registerUser(UserInfoDTO userInfoDTO) throws AlreadyBoundException, NullPointerException {
        if(userInfoDTO.getEmail().isEmpty() || userInfoDTO.getPassword().isEmpty() || userInfoDTO.getUsername().isEmpty())
        {
            throw new NullPointerException("Missing data from user");
        }
        if(userRepository.existsByEmail(userInfoDTO.getEmail()))
        {
            throw new AlreadyBoundException("Email already exists");
        }
        if(userRepository.existsByUsername(userInfoDTO.getUsername()))
        {
            throw new AlreadyBoundException("Username already exists");
        }
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail(userInfoDTO.getEmail());
        user.setPassword(hasher.getEncoder().encode(userInfoDTO.getPassword()));
        userRepository.save(user);
        JSONObject fullJson = new JSONObject();
        JSONObject userInfo = new JSONObject();
        fullJson.put("method", "Update User");
        userInfo.put("uuid", user.getId());
        userInfo.put("username", userInfoDTO.getUsername());
        userInfo.put("email", user.getEmail());
        fullJson.put("data", userInfo);
        rabbitMQSender.send(new Message(fullJson.toJSONString().getBytes(StandardCharsets.UTF_8)));
    }

    public boolean verifyToken(String token)
    {
        if (hasher.validateToken(token))
        {
            return true;
        }
        return false;
    }
}
