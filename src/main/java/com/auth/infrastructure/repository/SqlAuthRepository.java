package com.auth.infrastructure.repository;

import com.auth.domain.User;
import com.auth.domain.ports.AuthRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SqlAuthRepository implements AuthRepository {

    private final JpaAuthRepository jpaAuthRepository;

    public SqlAuthRepository(JpaAuthRepository jpaAuthRepository) {
        this.jpaAuthRepository = jpaAuthRepository;
    }

    @Override
    public User findByUsername(String username) {
        final var userEntity = jpaAuthRepository.findByUsername(username);
        return toDomain(userEntity);
    }

    @Override
    public List<User> findAll() {
        return jpaAuthRepository
                .findAll()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    private User toDomain(UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }
        return new User(
                userEntity.getId(),
                userEntity.getUsername(),
                userEntity.getEmail(),
                userEntity.getEncryptedPassword(),
                userEntity.getRole()
        );
    }
}
