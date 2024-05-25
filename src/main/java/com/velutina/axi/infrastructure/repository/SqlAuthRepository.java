package com.velutina.axi.infrastructure.repository;

import com.velutina.axi.domain.ports.AuthRepository;
import com.velutina.axi.domain.User;
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
        return new User(
                userEntity.getId(),
                userEntity.getUsername(),
                userEntity.getEmail(),
                userEntity.getEncryptedPassword(),
                userEntity.getRole()
        );
    }
}
