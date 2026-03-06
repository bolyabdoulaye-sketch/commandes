package com.polytech.commandes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String username;
    private String role;
    private long expiresIn; // durée en secondes
}
