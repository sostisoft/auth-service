package com.auth.infrastructure.controller;

import com.auth.applicacion.AuthService;
import com.auth.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private List<User> userList;

    @BeforeEach
    void setUp() {
        userList = List.of(new User(1L, "testuser", "testuser@example.com", "encryptedpassword", "user"));
    }

    @Test
    void testLogin_Success() {
        LoginRequest loginRequest = new LoginRequest("testuser", "password");
        when(authService.authenticate("testuser", "password")).thenReturn("validToken");

        ResponseEntity<String> response = authController.login(loginRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("validToken", response.getBody());

        verify(authService, times(1)).authenticate("testuser", "password");
    }

    @Test
    void testLogin_Failure() {
        LoginRequest loginRequest = new LoginRequest("testuser", "wrongpassword");
        when(authService.authenticate("testuser", "wrongpassword")).thenReturn(null);

        ResponseEntity<String> response = authController.login(loginRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Credenciales incorrectas", response.getBody());

        verify(authService, times(1)).authenticate("testuser", "wrongpassword");
    }

    @Test
    void testGetUsers_Success() {
        when(authService.getUsers("validToken")).thenReturn(userList);

        ResponseEntity<List<UserControllerEntity>> response = authController.getUsers("validToken");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("testuser", response.getBody().get(0).getUsername());

        verify(authService, times(1)).getUsers("validToken");
    }

    @Test
    void testGetUsers_Failure() {
        when(authService.getUsers("invalidToken")).thenThrow(new RuntimeException());

        ResponseEntity<List<UserControllerEntity>> response = authController.getUsers("invalidToken");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());

        verify(authService, times(1)).getUsers("invalidToken");
    }

    @Test
    void testGetTimeLeftInToken_Success() {
        when(authService.getTimeLeftInToken("validToken")).thenReturn(3600L);

        ResponseEntity<Long> response = authController.getTimeLeftInToken("validToken");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(3600L, response.getBody());

        verify(authService, times(1)).getTimeLeftInToken("validToken");
    }

    @Test
    void testGetTimeLeftInToken_Failure() {
        when(authService.getTimeLeftInToken("invalidToken")).thenThrow(new RuntimeException());

        ResponseEntity<Long> response = authController.getTimeLeftInToken("invalidToken");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(0L, response.getBody());

        verify(authService, times(1)).getTimeLeftInToken("invalidToken");
    }
}
