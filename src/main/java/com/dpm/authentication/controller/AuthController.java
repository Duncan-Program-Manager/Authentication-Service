package com.dpm.authentication.controller;

import com.dpm.authentication.dto.UserInfoDTO;
import com.dpm.authentication.endpoint.AuthEndpoints;
import com.dpm.authentication.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.rmi.AlreadyBoundException;
import java.util.StringTokenizer;

@RestController
@RequestMapping(AuthEndpoints.BASE)
public class AuthController {

    private AuthService authService;

    @Autowired
    public AuthController(AuthService authService)
    {
        this.authService = authService;
    }

    @PostMapping(value = AuthEndpoints.LOGIN)
    public ResponseEntity<?> loginUser(@RequestBody UserInfoDTO userInfoDto)
    {
        String token = authService.loginUser(userInfoDto.getEmail(), userInfoDto.getPassword());
        if (token == null || token.isEmpty()|| token.equals("")) {
            return new ResponseEntity<>("The email or password is wrong", HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @PostMapping(value = AuthEndpoints.REGISTER)
    public ResponseEntity<?> registerUser(@RequestBody UserInfoDTO userInfoDTO)
    {
        try {
            authService.registerUser(userInfoDTO);
        } catch (AlreadyBoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = AuthEndpoints.VERIFY)
    public ResponseEntity<?> verifyToken(@RequestBody String body)
    {
        final StringTokenizer tokenizer = new StringTokenizer(body, ":");
        final String token = tokenizer.nextToken();

        if(authService.verifyToken(token))
        {
            return new ResponseEntity<>("The token is valid", HttpStatus.OK);
        }
        return new ResponseEntity<>("The token is invalid", HttpStatus.BAD_REQUEST);
    }
}
