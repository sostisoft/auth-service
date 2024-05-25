package com.auth.domain.ports;

import com.auth.domain.User;

public interface TokenGenerator {

    String generateToken(User user);

    boolean validateToken(String authToken);

    String processToken(String token);

    long getTimeLeftInToken(String token);
}
