package com.polytech.commandes.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dateCommande;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CommandeStatus status;

    @Column(precision = 10, scale = 2)
    private BigDecimal montantTotal;

    // Pas de relation JPA - juste l'ID
    @Column(name = "client_id", nullable = false)
    private Long clientId;

    @PrePersist
    public void prePersist() {
        if (dateCommande == null) {
            dateCommande = LocalDateTime.now();
        }
        if (status == null) {
            status = CommandeStatus.CREATED;
        }
    }
}