package com.velutina.axi.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaAuthRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByUsername(String username);

}
