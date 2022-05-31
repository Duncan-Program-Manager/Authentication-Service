package com.dpm.authentication.controller;

import com.dpm.authentication.dto.UserInfoDTO;
import com.dpm.authentication.endpoint.UserEndpoints;
import com.dpm.authentication.service.UserService;
import org.apache.catalina.User;
import org.apache.commons.lang.NotImplementedException;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;

@RestController
@RequestMapping(UserEndpoints.BASE)
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService)
    {
        this.userService = userService;
    }

    @PostMapping(value = UserEndpoints.UPDATEUSER)
    public ResponseEntity<?> updateUser(@RequestBody UserInfoDTO userInfoDTO)
    {
        try {
            userService.updateUser(userInfoDTO);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = UserEndpoints.DELETEUSER)
    public ResponseEntity<?> deleteUser()
    {
        throw new NotImplementedException();
    }
}
