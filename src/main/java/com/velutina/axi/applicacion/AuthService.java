package com.velutina.axi.applicacion;

import com.velutina.axi.domain.User;
import com.velutina.axi.domain.ports.AuthRepository;
import com.velutina.axi.domain.ports.PasswordEncryption;
import com.velutina.axi.domain.ports.TokenGenerator;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


@Service
public class AuthService {

    private final TokenGenerator tokenGenerator;

    private final AuthRepository authRepository;

    private final PasswordEncryption passwordEncryption;


    public AuthService(TokenGenerator tokenGenerator, AuthRepository authRepository, PasswordEncryption passwordEncryption) {
        this.tokenGenerator = tokenGenerator;
        this.authRepository = authRepository;
        this.passwordEncryption = passwordEncryption;
    }

    public String authenticate(String username, String password) {
        final var user = authRepository.findByUsername(username);

        if (user != null && passwordEncryption.checkPassword(user, password)) {
            return tokenGenerator.generateToken(user);
        }

        return null;
    }

    public List<User> getUsers(String token) {
        if (!tokenGenerator.validateToken(token)) {
            return Collections.emptyList();
        }

        String role = tokenGenerator.processToken(token);

        if (role != null && role.equals("admin")) {
            return authRepository.findAll();
        }

        return Collections.emptyList();
    }

    public long getTimeLeftInToken(String token) {
        try {
            return tokenGenerator.getTimeLeftInToken(token);
        } catch (Exception e) {
            return 0;
        }
    }
}
