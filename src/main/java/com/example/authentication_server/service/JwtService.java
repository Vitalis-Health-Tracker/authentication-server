package com.example.authentication_server.service;

import com.example.authentication_server.model.RoleDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {
    private static final String SECRET_KEY = generateKey();

    public static String generateKey() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] keyBytes = new byte[32];
        secureRandom.nextBytes(keyBytes);
        return Base64.getEncoder().encodeToString(keyBytes);
    }

    private Key getSignKey()
    {
        byte[] keyBytes= Base64.getDecoder().decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String email, String role) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, email, role);
    }

    private String createToken(Map<String, Object> claims, String email, String role)
    {
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+(86400*1000)))
                .claim("Role", role)
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public void validateToken(String token) {
        Jwts
                .parser()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token);
    }

    public String getRoleFromToken(String token) {
        String role = Jwts
                .parser()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("Role", String.class);
        return role;
    }
}