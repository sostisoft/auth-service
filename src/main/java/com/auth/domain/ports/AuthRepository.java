package com.auth.domain.ports;
import com.auth.domain.User;

import java.util.List;

public interface AuthRepository {

    User findByUsername(String username);

    List<User> findAll();

}
