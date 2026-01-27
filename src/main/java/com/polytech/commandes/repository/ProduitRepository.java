package com.polytech.commandes.repository;

import com.polytech.commandes.entity.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProduitRepository extends JpaRepository<Produit, Long> {

    // Méthode métier 1
    List<Produit> findByStockGreaterThan(Integer stock);

    // Méthode métier 2
    @Query("SELECT p FROM Produit p WHERE p.stock < :seuil")
    List<Produit> findProduitsStockFaible(Integer seuil);
}