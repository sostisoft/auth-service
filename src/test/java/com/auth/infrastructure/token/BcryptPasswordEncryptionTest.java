package com.auth.infrastructure.token;

import com.auth.domain.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BcryptPasswordEncryptionTest {

    private final BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

    private BcryptPasswordEncryption bcryptPasswordEncryption;

    @BeforeEach
    public void setUp() {
       bcryptPasswordEncryption = new BcryptPasswordEncryption(bCryptPasswordEncoder);
    }

    @Test
    public void testEncrypt() {
        String rawPassword = "password123";
        String encodedPassword = "$2a$10$DowJonesIndex...";

        when(bCryptPasswordEncoder.encode(rawPassword)).thenReturn(encodedPassword);

        String result = bcryptPasswordEncryption.encrypt(rawPassword);

        assertEquals(encodedPassword, result);
    }

    @Test
    public void testCheckPassword() {
        String rawPassword = "password123";
        String encodedPassword = "$2a$10$DowJonesIndex...";
        User user = new User();
        user.setEncryptedPassword(encodedPassword);

        when(bCryptPasswordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);

        boolean result = bcryptPasswordEncryption.checkPassword(user, rawPassword);

        assertTrue(result);
    }
}
