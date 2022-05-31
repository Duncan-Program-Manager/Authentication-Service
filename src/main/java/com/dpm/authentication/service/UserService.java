package com.dpm.authentication.service;

import com.dpm.authentication.datamodels.User;
import com.dpm.authentication.dto.UserInfoDTO;
import com.dpm.authentication.logic.PasswordHasher;
import com.dpm.authentication.repository.UserRepository;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;
    private PasswordHasher hasher;

    @Autowired
    public UserService(UserRepository userRepository, PasswordHasher hasher)
    {
        this.userRepository = userRepository;
        this.hasher = hasher;
    }

    public void updateUser(UserInfoDTO userInfoDTO) throws AuthenticationException, NullPointerException {
        Optional<User> userFromDatabase = userRepository.findByEmail(userInfoDTO.getEmail());
        if(userFromDatabase.isEmpty())
        {
            throw new NullPointerException("User not found");
        }
        User user = userFromDatabase.get();
        if(hasher.getEncoder().matches(userInfoDTO.getPassword(), user.getPassword()))
        {
            user.setUsername(userInfoDTO.getUsername());
            user.setEmail(userInfoDTO.getEmail());
            user.setPassword(hasher.getEncoder().encode(userInfoDTO.getPassword()));
            userRepository.save(user);
        }
        throw new AuthenticationException("Match of email and password not found");
    }

}
