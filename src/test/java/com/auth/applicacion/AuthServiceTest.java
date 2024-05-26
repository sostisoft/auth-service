package com.auth.applicacion;

import static org.junit.jupiter.api.Assertions.*;

import com.auth.domain.User;
import com.auth.domain.ports.AuthRepository;
import com.auth.domain.ports.PasswordEncryption;
import com.auth.domain.ports.TokenGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private TokenGenerator tokenGenerator;

    @Mock
    private AuthRepository authRepository;

    @Mock
    private PasswordEncryption passwordEncryption;

    @InjectMocks
    private AuthService authService;

    private User user;
    private List<User> userList;

    @BeforeEach
    void setUp() {
        user = new User(1L, "testuser", "testuser@example.com", "encryptedpassword", "user");
        userList = Arrays.asList(user);
    }

    @Test
    void testAuthenticate_Success() {
        when(authRepository.findByUsername("testuser")).thenReturn(user);
        when(passwordEncryption.checkPassword(user, "password")).thenReturn(true);
        when(tokenGenerator.generateToken(user)).thenReturn("validToken");

        String token = authService.authenticate("testuser", "password");

        assertEquals("validToken", token);
        verify(authRepository, times(1)).findByUsername("testuser");
        verify(passwordEncryption, times(1)).checkPassword(user, "password");
        verify(tokenGenerator, times(1)).generateToken(user);
    }

    @Test
    void testAuthenticate_Failure() {
        when(authRepository.findByUsername("testuser")).thenReturn(user);
        when(passwordEncryption.checkPassword(user, "wrongpassword")).thenReturn(false);

        String token = authService.authenticate("testuser", "wrongpassword");

        assertNull(token);
        verify(authRepository, times(1)).findByUsername("testuser");
        verify(passwordEncryption, times(1)).checkPassword(user, "wrongpassword");
        verify(tokenGenerator, never()).generateToken(any(User.class));
    }

    @Test
    void testGetUsers_AdminRole() {
        when(tokenGenerator.validateToken("validAdminToken")).thenReturn(true);
        when(tokenGenerator.processToken("validAdminToken")).thenReturn("admin");
        when(authRepository.findAll()).thenReturn(userList);

        List<User> users = authService.getUsers("validAdminToken");

        assertEquals(1, users.size());
        assertEquals("testuser", users.get(0).getUsername());
        verify(tokenGenerator, times(1)).validateToken("validAdminToken");
        verify(tokenGenerator, times(1)).processToken("validAdminToken");
        verify(authRepository, times(1)).findAll();
    }

    @Test
    void testGetUsers_InvalidToken() {
        when(tokenGenerator.validateToken("invalidToken")).thenReturn(false);

        List<User> users = authService.getUsers("invalidToken");

        assertTrue(users.isEmpty());
        verify(tokenGenerator, times(1)).validateToken("invalidToken");
        verify(tokenGenerator, never()).processToken(anyString());
        verify(authRepository, never()).findAll();
    }

    @Test
    void testGetUsers_NonAdminRole() {
        when(tokenGenerator.validateToken("validUserToken")).thenReturn(true);
        when(tokenGenerator.processToken("validUserToken")).thenReturn("user");

        List<User> users = authService.getUsers("validUserToken");

        assertTrue(users.isEmpty());
        verify(tokenGenerator, times(1)).validateToken("validUserToken");
        verify(tokenGenerator, times(1)).processToken("validUserToken");
        verify(authRepository, never()).findAll();
    }

    @Test
    void testGetTimeLeftInToken_Success() {
        when(tokenGenerator.getTimeLeftInToken("validToken")).thenReturn(3600L);

        long timeLeft = authService.getTimeLeftInToken("validToken");

        assertEquals(3600L, timeLeft);
        verify(tokenGenerator, times(1)).getTimeLeftInToken("validToken");
    }

    @Test
    void testGetTimeLeftInToken_Failure() {
        when(tokenGenerator.getTimeLeftInToken("invalidToken")).thenThrow(new RuntimeException());

        long timeLeft = authService.getTimeLeftInToken("invalidToken");

        assertEquals(0L, timeLeft);
        verify(tokenGenerator, times(1)).getTimeLeftInToken("invalidToken");
    }
}
