package com.velutina.axi.repository;


import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.velutina.axi.model.User;

@Repository
public interface AuthRepository extends JpaRepository <User, Long> {

    User findByUsername(String username);

    @SuppressWarnings("null")
    List<User> findAll();
}
