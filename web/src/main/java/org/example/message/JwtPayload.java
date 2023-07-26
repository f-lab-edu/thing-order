package org.example.message;

import lombok.Getter;

@Getter
public class JwtPayload {
    private final Long id;

    private final String userEmail;

    private final String issuer;

    public JwtPayload(Long id, String userEmail, String issuer) {
        if (id == null || userEmail == null || issuer == null) {
            throw new IllegalArgumentException("jwt payload can not be null");
        }

        this.id = id;
        this.userEmail = userEmail;
        this.issuer = issuer;
    }
}
