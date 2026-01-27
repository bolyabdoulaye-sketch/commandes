package com.polytech.commandes.config;

import com.polytech.commandes.dto.ClientDTO;
import com.polytech.commandes.dto.CommandeDTO;
import com.polytech.commandes.dto.LigneCommandeDTO;
import com.polytech.commandes.dto.ProduitDTO;
import com.polytech.commandes.service.ClientService;
import com.polytech.commandes.service.CommandeService;
import com.polytech.commandes.service.ProduitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@Profile("dev")  // ACTIF UNIQUEMENT SUR LE PROFIL DEV
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final ClientService clientService;
    private final ProduitService produitService;
    private final CommandeService commandeService;

    @Override
    public void run(String... args) throws Exception {
        log.info("=== Initialisation des données de test (profil dev) ===");

        // Créer des clients
        ClientDTO client1 = clientService.create(ClientDTO.builder()
                .nom("Abdoulaye Boly")
                .email("boly.abdoulaye@ugb.edu.sn")
                .build());

        ClientDTO client2 = clientService.create(ClientDTO.builder()
                .nom("Moda Seck")
                .email("seck.moda@ugb.edu.sn")
                .build());

        ClientDTO client3 = clientService.create(ClientDTO.builder()
                .nom("Fatou Fall")
                .email("fall.Fatou@ugb.edu.sn")
                .build());

        log.info("✓ {} clients créés", 3);

        // Créer des produits
        ProduitDTO produit1 = produitService.create(ProduitDTO.builder()
                .nom("Ordinateur Portable Lenovo")
                .prix(new BigDecimal("850000"))
                .stock(15)
                .build());

        ProduitDTO produit2 = produitService.create(ProduitDTO.builder()
                .nom("Clavier Mécanique")
                .prix(new BigDecimal("35000"))
                .stock(50)
                .build());

        ProduitDTO produit3 = produitService.create(ProduitDTO.builder()
                .nom("Souris Sans Fil")
                .prix(new BigDecimal("15000"))
                .stock(100)
                .build());

        ProduitDTO produit4 = produitService.create(ProduitDTO.builder()
                .nom("Écran 24 pouces")
                .prix(new BigDecimal("120000"))
                .stock(25)
                .build());

        ProduitDTO produit5 = produitService.create(ProduitDTO.builder()
                .nom("Webcam HD")
                .prix(new BigDecimal("45000"))
                .stock(30)
                .build());

        log.info("✓ {} produits créés", 5);

        // Créer une commande complète pour client1
        CommandeDTO commande1 = CommandeDTO.builder()
                .clientId(client1.getId())
                .lignes(List.of(
                        LigneCommandeDTO.builder()
                                .produitId(produit1.getId())
                                .quantite(1)
                                .build(),
                        LigneCommandeDTO.builder()
                                .produitId(produit2.getId())
                                .quantite(2)
                                .build(),
                        LigneCommandeDTO.builder()
                                .produitId(produit3.getId())
                                .quantite(2)
                                .build()
                ))
                .build();

        CommandeDTO commandeCreee1 = commandeService.create(commande1);
        log.info("✓ Commande créée (ID: {}, Montant: {} FCFA)", commandeCreee1.getId(), commandeCreee1.getMontantTotal());

        // Créer une deuxième commande pour client2
        CommandeDTO commande2 = CommandeDTO.builder()
                .clientId(client2.getId())
                .lignes(List.of(
                        LigneCommandeDTO.builder()
                                .produitId(produit4.getId())
                                .quantite(1)
                                .build(),
                        LigneCommandeDTO.builder()
                                .produitId(produit3.getId())
                                .quantite(3)
                                .build(),
                        LigneCommandeDTO.builder()
                                .produitId(produit5.getId())
                                .quantite(1)
                                .build()
                ))
                .build();

        CommandeDTO commandeCreee2 = commandeService.create(commande2);
        log.info("✓ Commande créée (ID: {}, Montant: {} FCFA)", commandeCreee2.getId(), commandeCreee2.getMontantTotal());

        // Créer une troisième commande pour client3
        CommandeDTO commande3 = CommandeDTO.builder()
                .clientId(client3.getId())
                .lignes(List.of(
                        LigneCommandeDTO.builder()
                                .produitId(produit1.getId())
                                .quantite(1)
                                .build(),
                        LigneCommandeDTO.builder()
                                .produitId(produit4.getId())
                                .quantite(1)
                                .build()
                ))
                .build();

        CommandeDTO commandeCreee3 = commandeService.create(commande3);
        log.info("✓ Commande créée (ID: {}, Montant: {} FCFA)", commandeCreee3.getId(), commandeCreee3.getMontantTotal());

        log.info("=== Initialisation terminée avec succès ===");
        log.info("=== Total: {} clients, {} produits, {} commandes ===", 3, 5, 3);
    }
}