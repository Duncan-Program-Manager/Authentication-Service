package com.dpm.authentication.service;

import com.dpm.authentication.datamodels.User;
import com.dpm.authentication.dto.UserInfoDTO;
import com.dpm.authentication.logic.PasswordHasher;
import com.dpm.authentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.rmi.AlreadyBoundException;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    private UserRepository userRepository;
    private PasswordHasher hasher;

    @Autowired
    public AuthService(UserRepository userRepository, PasswordHasher hasher)
    {
        this.userRepository = userRepository;
        this.hasher = hasher;
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
        user.setUsername(userInfoDTO.getUsername());
        user.setPassword(hasher.getEncoder().encode(userInfoDTO.getPassword()));
        userRepository.save(user);
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
