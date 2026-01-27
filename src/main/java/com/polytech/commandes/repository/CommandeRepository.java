package com.polytech.commandes.repository;

import com.polytech.commandes.entity.Commande;
import com.polytech.commandes.entity.CommandeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CommandeRepository extends JpaRepository<Commande, Long> {

    // Méthode métier 1
    List<Commande> findByClientId(Long clientId);

    // Méthode métier 2
    List<Commande> findByStatus(CommandeStatus status);

    // Méthode métier supplémentaire
    @Query("SELECT c FROM Commande c WHERE c.dateCommande BETWEEN :debut AND :fin")
    List<Commande> findCommandesPeriode(LocalDateTime debut, LocalDateTime fin);
}