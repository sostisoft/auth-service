package com.velutina.axi.domain.ports;

import com.velutina.axi.domain.User;

public interface PasswordEncryption {

    boolean checkPassword(User user, String password);

    String encrypt(String password);
}
