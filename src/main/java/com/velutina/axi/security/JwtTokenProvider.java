package com.velutina.axi.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.velutina.axi.model.User;

import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    public String generateToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        Claims claims = Jwts.claims().setSubject(user.getUsername());
        claims.put("id", user.getId());
        claims.put("email", user.getEmail());
        claims.put("role", user.getRole());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            parseClaims(authToken); 
            return true;
        } catch (Exception ex) {
            ex.printStackTrace(); 
            return false;
        }
    }

    public String processToken(String token) {
        try {
            Claims claims = parseClaims(token); 

            if (claims != null) {
                String role = (String) claims.get("role");

                if (role != null) {
                    if (role.equals("admin")) {
                        return "admin";
                    } else if (role.equals("user")) {
                        return "user";
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (SignatureException e) {
            System.out.println("Token inválido o no válido.");
            e.printStackTrace();
            return null; // Devuelve null en caso de error de firma
        }
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        } catch (Exception ex) {
            // Captura cualquier excepción durante el parsing del token
            ex.printStackTrace(); // Imprime la traza de la excepción
            return null; // Devuelve null en caso de error de validación del token
        }
    }

    public long getTimeLeftInToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token)
                    .getBody();

            Date expirationDate = claims.getExpiration();
            if (expirationDate == null) {
                return 0; // El token no tiene fecha de expiración válida
            }

            long currentTimeMillis = System.currentTimeMillis();
            long expirationTimeMillis = expirationDate.getTime();
            return expirationTimeMillis - currentTimeMillis;
        } catch (Exception ex) {
            ex.printStackTrace(); // Maneja cualquier error al procesar el token
            return 0; // Devuelve 0 si ocurre un error
        }
    }

}