package com.polytech.commandes.controller;

import com.polytech.commandes.dto.AuthRequest;
import com.polytech.commandes.dto.AuthResponse;
import com.polytech.commandes.entity.Utilisateur;
import com.polytech.commandes.repository.UtilisateurRepository;
import com.polytech.commandes.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentification", description = "Inscription et connexion avec JWT")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    @Operation(summary = "Créer un compte utilisateur")
    public ResponseEntity<?> register(@Valid @RequestBody AuthRequest request) {

        // Vérifier si le nom d'utilisateur existe déjà
        if (utilisateurRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Nom d'utilisateur déjà pris : " + request.getUsername()));
        }

        // Créer et sauvegarder l'utilisateur
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setUsername(request.getUsername());
        utilisateur.setPassword(passwordEncoder.encode(request.getPassword()));
        utilisateur.setRole(Utilisateur.Role.USER);
        utilisateurRepository.save(utilisateur);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of("message", "Compte créé avec succès pour : " + request.getUsername()));
    }

    @PostMapping("/login")
    @Operation(summary = "Se connecter et obtenir un token JWT")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest request) {
        try {
            // Authentifier l'utilisateur (lance une exception si échec)
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Nom d'utilisateur ou mot de passe incorrect"));
        }

        // Générer le token
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtUtil.generateToken(userDetails);

        // Récupérer le rôle
        String role = userDetails.getAuthorities().iterator().next().getAuthority();

        return ResponseEntity.ok(new AuthResponse(
                token,
                request.getUsername(),
                role,
                jwtUtil.getExpirationInSeconds()
        ));
    }
}
