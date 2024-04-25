package com.vetulina.auth.service;

import com.vetulina.auth.model.User;
import com.vetulina.auth.repository.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthService {
    @Autowired
    private AuthRepository authRepository;

    public String authenticate(String username, String password) {
        User user = authRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            // Generar y devolver un token de sesión único
            return generateToken();
        } else {
            return null;
        }
    }

    private String generateToken() {
        // Implementación para generar un token de sesión único
        return UUID.randomUUID().toString();
    }
}
