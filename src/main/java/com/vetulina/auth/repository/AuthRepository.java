package com.vetulina.auth.repository;

import com.vetulina.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository extends JpaRepository <User, Long> {
    User findByUsername(String username);
}
