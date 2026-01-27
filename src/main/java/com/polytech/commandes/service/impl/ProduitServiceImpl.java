package com.polytech.commandes.service.impl;

import com.polytech.commandes.dto.ProduitDTO;
import com.polytech.commandes.entity.Produit;
import com.polytech.commandes.exception.ResourceNotFoundException;
import com.polytech.commandes.repository.ProduitRepository;
import com.polytech.commandes.service.ProduitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProduitServiceImpl implements ProduitService {

    private final ProduitRepository produitRepository;

    @Override
    public ProduitDTO create(ProduitDTO produitDTO) {
        Produit produit = Produit.builder()
                .nom(produitDTO.getNom())
                .prix(produitDTO.getPrix())
                .stock(produitDTO.getStock())
                .build();

        Produit saved = produitRepository.save(produit);
        return toDTO(saved);
    }

    @Override
    public ProduitDTO update(Long id, ProduitDTO produitDTO) {
        Produit produit = produitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produit non trouvé avec l'ID: " + id));

        produit.setNom(produitDTO.getNom());
        produit.setPrix(produitDTO.getPrix());
        produit.setStock(produitDTO.getStock());

        Produit updated = produitRepository.save(produit);
        return toDTO(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public ProduitDTO findById(Long id) {
        Produit produit = produitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produit non trouvé avec l'ID: " + id));
        return toDTO(produit);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProduitDTO> findAll() {
        return produitRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        if (!produitRepository.existsById(id)) {
            throw new ResourceNotFoundException("Produit non trouvé avec l'ID: " + id);
        }
        produitRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProduitDTO> findProduitsDisponibles() {
        return produitRepository.findByStockGreaterThan(0).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProduitDTO> findProduitsStockFaible(Integer seuil) {
        return produitRepository.findProduitsStockFaible(seuil).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private ProduitDTO toDTO(Produit produit) {
        return ProduitDTO.builder()
                .id(produit.getId())
                .nom(produit.getNom())
                .prix(produit.getPrix())
                .stock(produit.getStock())
                .build();
    }
}