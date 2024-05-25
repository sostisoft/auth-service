package com.auth.domain.ports;

import com.auth.domain.User;

public interface PasswordEncryption {

    boolean checkPassword(User user, String password);

    String encrypt(String password);
}
