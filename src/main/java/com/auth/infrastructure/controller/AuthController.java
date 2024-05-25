package com.auth.infrastructure.controller;


import com.auth.domain.User;
import com.auth.applicacion.AuthService;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        String token = authService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());

        if (token != null) {
            return ResponseEntity.ok(token);
        }

        return new ResponseEntity<>("Credenciales incorrectas", HttpStatusCode.valueOf(400));

    }

    @GetMapping("/getUsers")
    public ResponseEntity<List<UserControllerEntity>> getUsers(String token) {
        try{
            final var users = authService.getUsers(token);
            final var usersToReturn = from(users);
            return ResponseEntity.ok(usersToReturn);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/timeLeft")
    public ResponseEntity<Long> getTimeLeftInToken(String token) {
        try{
            return ResponseEntity.ok(authService.getTimeLeftInToken(token));
        }catch(Exception e){
            return new ResponseEntity<>(0L, HttpStatusCode.valueOf(400));
        }
    }

    private List<UserControllerEntity> from(List<User> users) {
        return users
                .stream()
                .map(AuthController::toUserControllerEntity)
                .toList();
    }

    private static UserControllerEntity toUserControllerEntity(User user) {
        return new UserControllerEntity(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getRole());

    }
}
