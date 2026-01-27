package com.polytech.commandes.service;

import com.polytech.commandes.dto.CommandeDTO;
import com.polytech.commandes.entity.CommandeStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface CommandeService {

    CommandeDTO create(CommandeDTO commandeDTO);

    CommandeDTO findById(Long id);

    List<CommandeDTO> findAll();

    List<CommandeDTO> findByClientId(Long clientId);

    List<CommandeDTO> findByStatus(CommandeStatus status);

    CommandeDTO valider(Long id);

    CommandeDTO annuler(Long id);

    List<CommandeDTO> findCommandesPeriode(LocalDateTime debut, LocalDateTime fin);
}