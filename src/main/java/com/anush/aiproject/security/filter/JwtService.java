package com.anush.aiproject.security.filter;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.anush.aiproject.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import com.anush.aiproject.security.config.JwtProperties;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties jwtProperties;

    public String generateToken(User user) {
        long nowMs = System.currentTimeMillis();
        long expirationMs = jwtProperties.getExpiration().toMillis();

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("role", user.getRole().name())
                .claim("userId", user.getId())
                .issuer(jwtProperties.getIssuer())
                .issuedAt(new Date(nowMs))
                .expiration(new Date(nowMs + expirationMs))
                .signWith(getSignKey())
                .compact();
    }

    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String extractRole(String token) {

        return extractAllClaims(token).get("role", String.class);
    }

    public String extractIssuer(String token) {

        return extractAllClaims(token).getIssuer();
    }

    public boolean isTokenValid(String token, User user) {
        try {
            String email = extractEmail(token);
            String issuer = extractIssuer(token);

            return email.equals(user.getEmail()) && !isTokenExpired(token) && issuer.equals(jwtProperties.getIssuer());
        } catch (Exception e) {
            return false;
        }

    }

    // helpers
    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecretKey());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private boolean isTokenExpired(String token) {
        long nowMs = System.currentTimeMillis();
        return extractAllClaims(token)
                .getExpiration()
                .before(new Date(nowMs));
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .requireIssuer(jwtProperties.getIssuer())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
