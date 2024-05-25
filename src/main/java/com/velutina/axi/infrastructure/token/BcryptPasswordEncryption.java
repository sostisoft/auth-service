package com.velutina.axi.infrastructure.token;

import com.velutina.axi.domain.User;
import com.velutina.axi.domain.ports.PasswordEncryption;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BcryptPasswordEncryption implements PasswordEncryption {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public BcryptPasswordEncryption(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public boolean checkPassword(User user, String password) {
        return bCryptPasswordEncoder.matches(password, user.getEncryptedPassword());
    }

    @Override
    public String encrypt(String password) {
        return bCryptPasswordEncoder.encode(password);
    }
}
