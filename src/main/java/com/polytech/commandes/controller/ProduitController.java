package com.polytech.commandes.controller;

import com.polytech.commandes.dto.ProduitDTO;
import com.polytech.commandes.service.ProduitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produits")
@RequiredArgsConstructor
@Tag(name = "Produits", description = "Gestion des produits")
public class ProduitController {

    private final ProduitService produitService;

    @Operation(summary = "Créer un produit")
    @PostMapping
    public ResponseEntity<ProduitDTO> create(@Valid @RequestBody ProduitDTO produitDTO) {
        ProduitDTO created = produitService.create(produitDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Récupérer tous les produits")
    @GetMapping
    public ResponseEntity<List<ProduitDTO>> findAll() {
        return ResponseEntity.ok(produitService.findAll());
    }

    @Operation(summary = "Récupérer un produit par ID")
    @GetMapping("/{id}")
    public ResponseEntity<ProduitDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(produitService.findById(id));
    }

    @Operation(summary = "Mettre à jour un produit")
    @PutMapping("/{id}")
    public ResponseEntity<ProduitDTO> update(@PathVariable Long id, @Valid @RequestBody ProduitDTO produitDTO) {
        return ResponseEntity.ok(produitService.update(id, produitDTO));
    }

    @Operation(summary = "Supprimer un produit")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        produitService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Récupérer les produits disponibles (stock > 0)")
    @GetMapping("/disponibles")
    public ResponseEntity<List<ProduitDTO>> findProduitsDisponibles() {
        return ResponseEntity.ok(produitService.findProduitsDisponibles());
    }

    @Operation(summary = "Récupérer les produits avec stock faible")
    @GetMapping("/stock-faible")
    public ResponseEntity<List<ProduitDTO>> findProduitsStockFaible(@RequestParam(defaultValue = "10") Integer seuil) {
        return ResponseEntity.ok(produitService.findProduitsStockFaible(seuil));
    }
}