package com.polytech.commandes.service;

import com.polytech.commandes.dto.ProduitDTO;

import java.util.List;

public interface ProduitService {

    ProduitDTO create(ProduitDTO produitDTO);

    ProduitDTO update(Long id, ProduitDTO produitDTO);

    ProduitDTO findById(Long id);

    List<ProduitDTO> findAll();

    void delete(Long id);

    List<ProduitDTO> findProduitsDisponibles();

    List<ProduitDTO> findProduitsStockFaible(Integer seuil);
}