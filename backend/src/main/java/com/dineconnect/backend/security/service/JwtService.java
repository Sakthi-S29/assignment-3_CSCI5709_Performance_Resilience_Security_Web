package com.dineconnect.backend.security.service;

import com.dineconnect.backend.user.model.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    private final SecretKey secretKey;

    public JwtService(@Value("${jwt.secret.key}") String jwtSecretKey) {
        if (jwtSecretKey == null || jwtSecretKey.length() < 32) {
            throw new IllegalArgumentException("JWT secret key must be at least 32 characters.");
        }
        this.secretKey = Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String email, boolean isAdmin) {

        return Jwts
                .builder()
                .setSubject(email)
                .addClaims(Map.of("role", isAdmin ? Role.ADMIN.name() : Role.USER.name()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hour
                .signWith(secretKey)
                .compact();
    }

    public String extractEmail(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }




    public boolean isTokenValid(String jwt, String email) {
        return extractEmail(jwt).equals(email) && !isTokenExpired(jwt);
    }

    public boolean isTokenExpired(String jwt) {

        return Jwts
                .parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(jwt)
                .getBody().getExpiration().before(new Date());

    }
}
