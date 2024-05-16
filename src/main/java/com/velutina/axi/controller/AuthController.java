package com.velutina.axi.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

import com.velutina.axi.model.LoginRequest;
import com.velutina.axi.model.User;
import com.velutina.axi.service.AuthService;

@RestController
public class AuthController {

    @Autowired
    private AuthService authService;


    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest) {
        String token = authService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
        if (token != null) {
            return token;
        } else {
            return "Credenciales incorrectas";
        }
    }

    @GetMapping("/getUsers")
    public List<User> getUsers (String token){
        try{
            List<User> users = authService.getUsers(token);
            return users;
        } catch(Exception e){
            return null;
        }
    } 

    @GetMapping("/timeLeft")
    public long getTimeLeftInToken(String token){
        try{
            long timeLeftInToken = authService.getTimeLeftInToken(token);
            return timeLeftInToken;
        }catch(Exception e){
            return 0;
        }
    }



}
