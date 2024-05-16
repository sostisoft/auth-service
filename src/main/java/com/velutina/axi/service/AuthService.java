package com.velutina.axi.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.velutina.axi.model.*;
import com.velutina.axi.repository.AuthRepository;
import com.velutina.axi.security.JwtTokenProvider;

@Service
public class AuthService {

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public String authenticate(String username, String password) {
        User user = authRepository.findByUsername(username);
        if (user != null && user.checkPassword(password)) {
            // Generar y devolver un token JWT
            return jwtTokenProvider.generateToken(user);
        } else {
            return null; // Credenciales incorrectas
        }
    }

    public List<User> getUsers(String token) {
        if (jwtTokenProvider.validateToken(token)) {
            String role = jwtTokenProvider.processToken(token);
            if (role != null && role.equals("admin")) {
                List<User> users = authRepository.findAll();

                // Configura ObjectMapper para excluir la contraseña al serializar User a JSON
                ObjectMapper objectMapper = new ObjectMapper();
                //objectMapper.addMixIn(User.class, UserMixin.class);

                return users;
            }
        }
        return null;
    }

    public long getTimeLeftInToken(String token) {
        try {
            return jwtTokenProvider.getTimeLeftInToken(token);

        } catch (Exception e) {
            return 0;
        }
    }
}
