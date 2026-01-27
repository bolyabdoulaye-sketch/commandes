package com.polytech.commandes.controller;

import com.polytech.commandes.dto.CommandeDTO;
import com.polytech.commandes.entity.CommandeStatus;
import com.polytech.commandes.service.CommandeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/commandes")
@RequiredArgsConstructor
@Tag(name = "Commandes", description = "Gestion des commandes")
public class CommandeController {

    private final CommandeService commandeService;

    @Operation(summary = "Créer une commande")
    @PostMapping
    public ResponseEntity<CommandeDTO> create(@Valid @RequestBody CommandeDTO commandeDTO) {
        CommandeDTO created = commandeService.create(commandeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Récupérer toutes les commandes")
    @GetMapping
    public ResponseEntity<List<CommandeDTO>> findAll() {
        return ResponseEntity.ok(commandeService.findAll());
    }

    @Operation(summary = "Récupérer une commande par ID")
    @GetMapping("/{id}")
    public ResponseEntity<CommandeDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(commandeService.findById(id));
    }

    @Operation(summary = "Récupérer les commandes d'un client")
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<CommandeDTO>> findByClientId(@PathVariable Long clientId) {
        return ResponseEntity.ok(commandeService.findByClientId(clientId));
    }

    @Operation(summary = "Récupérer les commandes par statut")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<CommandeDTO>> findByStatus(@PathVariable CommandeStatus status) {
        return ResponseEntity.ok(commandeService.findByStatus(status));
    }

    @Operation(summary = "Valider une commande")
    @PutMapping("/{id}/valider")
    public ResponseEntity<CommandeDTO> valider(@PathVariable Long id) {
        return ResponseEntity.ok(commandeService.valider(id));
    }

    @Operation(summary = "Annuler une commande")
    @PutMapping("/{id}/annuler")
    public ResponseEntity<CommandeDTO> annuler(@PathVariable Long id) {
        return ResponseEntity.ok(commandeService.annuler(id));
    }

    @Operation(summary = "Récupérer les commandes d'une période")
    @GetMapping("/periode")
    public ResponseEntity<List<CommandeDTO>> findCommandesPeriode(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return ResponseEntity.ok(commandeService.findCommandesPeriode(debut, fin));
    }
}