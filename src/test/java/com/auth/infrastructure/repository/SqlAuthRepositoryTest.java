package com.auth.infrastructure.repository;

import com.auth.domain.User;
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
public class SqlAuthRepositoryTest {

    @Mock
    private JpaAuthRepository jpaAuthRepository;

    @InjectMocks
    private SqlAuthRepository sqlAuthRepository;

    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setUsername("testuser");
        userEntity.setEmail("testuser@example.com");
        userEntity.setEncryptedPassword("encryptedpassword");
        userEntity.setRole("user");
    }

    @Test
    void testFindByUsername() {
        when(jpaAuthRepository.findByUsername("testuser")).thenReturn(userEntity);

        User foundUser = sqlAuthRepository.findByUsername("testuser");

        assertNotNull(foundUser);
        assertEquals("testuser", foundUser.getUsername());
        assertEquals("testuser@example.com", foundUser.getEmail());
        assertEquals("encryptedpassword", foundUser.getEncryptedPassword());
        assertEquals("user", foundUser.getRole());

        verify(jpaAuthRepository, times(1)).findByUsername("testuser");
    }

    @Test
    void testFindByUsername_NotFound() {
        when(jpaAuthRepository.findByUsername("unknownuser")).thenReturn(null);

        User foundUser = sqlAuthRepository.findByUsername("unknownuser");

        assertNull(foundUser);

        verify(jpaAuthRepository, times(1)).findByUsername("unknownuser");
    }

    @Test
    void testFindAll() {
        UserEntity anotherUserEntity = new UserEntity();
        anotherUserEntity.setId(2L);
        anotherUserEntity.setUsername("anotheruser");
        anotherUserEntity.setEmail("anotheruser@example.com");
        anotherUserEntity.setEncryptedPassword("anotherpassword");
        anotherUserEntity.setRole("admin");

        List<UserEntity> userEntities = Arrays.asList(userEntity, anotherUserEntity);
        when(jpaAuthRepository.findAll()).thenReturn(userEntities);

        List<User> users = sqlAuthRepository.findAll();

        assertEquals(2, users.size());
        assertEquals("testuser", users.get(0).getUsername());
        assertEquals("anotheruser", users.get(1).getUsername());

        verify(jpaAuthRepository, times(1)).findAll();
    }
}
