package com.polytech.commandes.service.impl;

import com.polytech.commandes.dto.CommandeDTO;
import com.polytech.commandes.dto.LigneCommandeDTO;
import com.polytech.commandes.entity.*;
import com.polytech.commandes.exception.ResourceNotFoundException;
import com.polytech.commandes.exception.StockInsuffisantException;
import com.polytech.commandes.repository.ClientRepository;
import com.polytech.commandes.repository.CommandeRepository;
import com.polytech.commandes.repository.LigneCommandeRepository;
import com.polytech.commandes.repository.ProduitRepository;
import com.polytech.commandes.service.CommandeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CommandeServiceImpl implements CommandeService {

    private final CommandeRepository commandeRepository;
    private final LigneCommandeRepository ligneCommandeRepository;
    private final ClientRepository clientRepository;
    private final ProduitRepository produitRepository;

    @Override
    public CommandeDTO create(CommandeDTO commandeDTO) {
        // Vérifier que le client existe
        Client client = clientRepository.findById(commandeDTO.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client non trouvé avec l'ID: " + commandeDTO.getClientId()));

        // Créer la commande
        Commande commande = Commande.builder()
                .dateCommande(LocalDateTime.now())
                .status(CommandeStatus.CREATED)
                .clientId(commandeDTO.getClientId())
                .build();

        // Calculer le montant total et vérifier le stock
        BigDecimal montantTotal = BigDecimal.ZERO;

        for (LigneCommandeDTO ligneDTO : commandeDTO.getLignes()) {
            Produit produit = produitRepository.findById(ligneDTO.getProduitId())
                    .orElseThrow(() -> new ResourceNotFoundException("Produit non trouvé avec l'ID: " + ligneDTO.getProduitId()));

            // RÈGLE MÉTIER 1 : Interdiction d'une commande si le stock est insuffisant
            if (produit.getStock() < ligneDTO.getQuantite()) {
                throw new StockInsuffisantException(
                        "Stock insuffisant pour le produit: " + produit.getNom() +
                                ". Disponible: " + produit.getStock() + ", Demandé: " + ligneDTO.getQuantite()
                );
            }

            // RÈGLE MÉTIER 2 : Calcul automatique du total
            BigDecimal sousTotal = produit.getPrix().multiply(BigDecimal.valueOf(ligneDTO.getQuantite()));
            montantTotal = montantTotal.add(sousTotal);
        }

        commande.setMontantTotal(montantTotal);
        Commande savedCommande = commandeRepository.save(commande);

        // Créer les lignes de commande
        for (LigneCommandeDTO ligneDTO : commandeDTO.getLignes()) {
            Produit produit = produitRepository.findById(ligneDTO.getProduitId()).get();

            LigneCommande ligne = LigneCommande.builder()
                    .commandeId(savedCommande.getId())
                    .produitId(produit.getId())
                    .quantite(ligneDTO.getQuantite())
                    .prixUnitaire(produit.getPrix())
                    .build();

            ligneCommandeRepository.save(ligne);
        }

        return toDTO(savedCommande);
    }
    @Override
    @Transactional(readOnly = true)
    public CommandeDTO findById(Long id) {
        Commande commande = commandeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Commande non trouvée avec l'ID: " + id));
        return toDTO(commande);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommandeDTO> findAll() {
        return commandeRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommandeDTO> findByClientId(Long clientId) {
        return commandeRepository.findByClientId(clientId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommandeDTO> findByStatus(CommandeStatus status) {
        return commandeRepository.findByStatus(status).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CommandeDTO valider(Long id) {
        Commande commande = commandeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Commande non trouvée avec l'ID: " + id));

        if (commande.getStatus() != CommandeStatus.CREATED) {
            throw new IllegalStateException("Seules les commandes CREATED peuvent être validées");
        }

        // RÈGLE MÉTIER 3 : Mise à jour du stock après validation
        List<LigneCommande> lignes = ligneCommandeRepository.findByCommandeId(id);

        for (LigneCommande ligne : lignes) {
            Produit produit = produitRepository.findById(ligne.getProduitId())
                    .orElseThrow(() -> new ResourceNotFoundException("Produit non trouvé"));

            // Vérification du stock avant validation
            if (produit.getStock() < ligne.getQuantite()) {
                throw new StockInsuffisantException(
                        "Stock insuffisant pour le produit: " + produit.getNom()
                );
            }

            // Diminuer le stock
            produit.setStock(produit.getStock() - ligne.getQuantite());
            produitRepository.save(produit);
        }

        commande.setStatus(CommandeStatus.VALIDATED);
        Commande updated = commandeRepository.save(commande);

        return toDTO(updated);
    }

    @Override
    public CommandeDTO annuler(Long id) {
        Commande commande = commandeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Commande non trouvée avec l'ID: " + id));

        // Si la commande était validée, remettre le stock
        if (commande.getStatus() == CommandeStatus.VALIDATED) {
            List<LigneCommande> lignes = ligneCommandeRepository.findByCommandeId(id);

            for (LigneCommande ligne : lignes) {
                Produit produit = produitRepository.findById(ligne.getProduitId())
                        .orElseThrow(() -> new ResourceNotFoundException("Produit non trouvé"));

                produit.setStock(produit.getStock() + ligne.getQuantite());
                produitRepository.save(produit);
            }
        }

        commande.setStatus(CommandeStatus.CANCELLED);
        Commande updated = commandeRepository.save(commande);

        return toDTO(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommandeDTO> findCommandesPeriode(LocalDateTime debut, LocalDateTime fin) {
        return commandeRepository.findCommandesPeriode(debut, fin).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private CommandeDTO toDTO(Commande commande) {
        // Récupérer le client
        Client client = clientRepository.findById(commande.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client non trouvé"));

        // Récupérer les lignes
        List<LigneCommande> lignes = ligneCommandeRepository.findByCommandeId(commande.getId());

        List<LigneCommandeDTO> lignesDTO = lignes.stream()
                .map(this::toLigneDTO)
                .collect(Collectors.toList());

        return CommandeDTO.builder()
                .id(commande.getId())
                .dateCommande(commande.getDateCommande())
                .status(commande.getStatus())
                .clientId(commande.getClientId())
                .clientNom(client.getNom())
                .lignes(lignesDTO)
                .montantTotal(commande.getMontantTotal())
                .build();
    }

    private LigneCommandeDTO toLigneDTO(LigneCommande ligne) {
        Produit produit = produitRepository.findById(ligne.getProduitId())
                .orElseThrow(() -> new ResourceNotFoundException("Produit non trouvé"));

        return LigneCommandeDTO.builder()
                .id(ligne.getId())
                .produitId(ligne.getProduitId())
                .produitNom(produit.getNom())
                .quantite(ligne.getQuantite())
                .prixUnitaire(ligne.getPrixUnitaire())
                .sousTotal(ligne.getSousTotal())
                .build();
    }
}
