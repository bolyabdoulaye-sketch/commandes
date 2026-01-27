package com.polytech.commandes.repository;

import com.polytech.commandes.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    // Méthode métier 1
    Optional<Client> findByEmail(String email);

    // Méthode métier 2
    boolean existsByEmail(String email);
}