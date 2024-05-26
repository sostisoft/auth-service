package com.auth.infrastructure.token;

import static org.junit.jupiter.api.Assertions.*;

import com.auth.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JwtTokenProviderTest {

    @InjectMocks
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private User user;

    @Value("${app.jwtSecret}")
    private String jwtSecret = "MyJwtSecret";

    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs = 3600000;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtSecret", jwtSecret);
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtExpirationInMs", jwtExpirationInMs);
    }

    @Test
    public void testGenerateToken() {
        when(user.getUsername()).thenReturn("testuser");
        when(user.getId()).thenReturn(1L);
        when(user.getEmail()).thenReturn("testuser@example.com");
        when(user.getRole()).thenReturn("user");

        String token = jwtTokenProvider.generateToken(user);

        assertNotNull(token);

        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        assertEquals("testuser", claims.getSubject());
        assertEquals(1, claims.get("id"));
        assertEquals("testuser@example.com", claims.get("email"));
        assertEquals("user", claims.get("role"));
    }

    @Test
    public void testValidateToken() {
        String token = generateTestToken();

        boolean isValid = jwtTokenProvider.validateToken(token);

        assertTrue(isValid);
    }

    @Test
    public void testValidateToken_Invalid() {
        String invalidToken = generateInvalidTestToken();

        boolean isValid = jwtTokenProvider.validateToken(invalidToken);

        assertFalse(isValid);
    }

    @Test
    public void testProcessToken_AdminRole() {
        String token = generateTestTokenWithRole("admin");

        String role = jwtTokenProvider.processToken(token);

        assertEquals("admin", role);
    }

    @Test
    public void testProcessToken_UserRole() {
        String token = generateTestTokenWithRole("user");

        String role = jwtTokenProvider.processToken(token);

        assertEquals("user", role);
    }

    @Test
    public void testGetTimeLeftInToken() {
        String token = generateTestToken();

        long timeLeft = jwtTokenProvider.getTimeLeftInToken(token);

        assertTrue(timeLeft > 0);
    }

    private String generateTestToken() {
        return Jwts.builder()
                .setSubject("testuser")
                .claim("id", 1L)
                .claim("email", "testuser@example.com")
                .claim("role", "user")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    private String generateTestTokenWithRole(String role) {
        return Jwts.builder()
                .setSubject("testuser")
                .claim("id", 1L)
                .claim("email", "testuser@example.com")
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    private String generateInvalidTestToken() {
        return Jwts.builder()
                .setSubject("testuser")
                .claim("id", 1L)
                .claim("email", "testuser@example.com")
                .claim("role", "user")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .signWith(SignatureAlgorithm.HS512, "InvalidSecret")
                .compact();
    }
}
