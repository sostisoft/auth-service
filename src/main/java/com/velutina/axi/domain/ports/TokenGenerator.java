package com.velutina.axi.domain.ports;

import com.velutina.axi.domain.User;

public interface TokenGenerator {

    String generateToken(User user);

    boolean validateToken(String authToken);

    String processToken(String token);

    long getTimeLeftInToken(String token);
}
