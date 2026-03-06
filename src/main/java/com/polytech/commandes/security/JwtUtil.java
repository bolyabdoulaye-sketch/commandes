package com.polytech.commandes.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret:cle_secrete_polytech_commandes_2024_tres_longue}")
    private String secretKey;

    @Value("${jwt.expiration:86400000}") // 24h par défaut
    private long expiration;

    /**
     * Génère un token JWT pour un utilisateur
     */
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("roles", userDetails.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extrait le nom d'utilisateur depuis le token
     */
    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    /**
     * Vérifie si le token est valide pour un utilisateur donné
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            String username = extractUsername(token);
            return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Vérifie si le token est expiré
     */
    public boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    /**
     * Retourne la durée d'expiration en secondes
     */
    public long getExpirationInSeconds() {
        return expiration / 1000;
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignKey() {
        byte[] keyBytes = secretKey.getBytes();
        // S'assurer que la clé est assez longue pour HS256 (minimum 256 bits)
        if (keyBytes.length < 32) {
            throw new IllegalStateException("La clé JWT doit faire au moins 32 caractères");
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
