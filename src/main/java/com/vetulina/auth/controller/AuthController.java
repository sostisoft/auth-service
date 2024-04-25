package com.vetulina.auth.controller;

import com.vetulina.auth.model.LoginRequest;
import com.vetulina.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/login2")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password) {
        String token = authService.authenticate(username, password);
        if (token != null) {
            return token;
        } else {
            return "Credenciales incorrectas";
        }
    }
}
