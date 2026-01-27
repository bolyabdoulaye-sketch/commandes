package com.polytech.commandes.repository;

import com.polytech.commandes.entity.LigneCommande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LigneCommandeRepository extends JpaRepository<LigneCommande, Long> {

    // Méthode métier 1
    List<LigneCommande> findByCommandeId(Long commandeId);

    // Méthode métier 2
    List<LigneCommande> findByProduitId(Long produitId);
}