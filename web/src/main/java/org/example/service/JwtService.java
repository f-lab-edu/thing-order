package org.example.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.example.message.JwtPayload;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class JwtService {

    private final String jwtSecretKey;

    public JwtService(@Value("${jwt.secret-key}") String jwtSecretKey) {
        this.jwtSecretKey = jwtSecretKey;
    }

    public boolean verifyToken(String token) {
        if (token == null) {
            return false;
        }

        Jws<Claims> parsedToken = null;

        try {
            parsedToken = Jwts.parserBuilder()
                    .setSigningKey(this.jwtSecretKey.getBytes(StandardCharsets.UTF_8))
                    .build()
                    .parseClaimsJws(token);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public JwtPayload getJwtPayload(String token) {

        Claims parsedToken = Jwts.parserBuilder()
                .setSigningKey(this.jwtSecretKey.getBytes(StandardCharsets.UTF_8))
                .build()
                .parseClaimsJws(token).getBody();

        Long userId = parsedToken.get("id", Long.class);
        String userEmail = parsedToken.get("userEmail", String.class);
        String issuer = parsedToken.get("iss", String.class);

        return new JwtPayload(userId, userEmail, issuer);
    }
}
