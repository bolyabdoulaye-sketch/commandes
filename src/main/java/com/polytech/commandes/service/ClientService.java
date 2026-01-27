package com.polytech.commandes.service;

import com.polytech.commandes.dto.ClientDTO;

import java.util.List;

public interface ClientService {

    ClientDTO create(ClientDTO clientDTO);

    ClientDTO update(Long id, ClientDTO clientDTO);

    ClientDTO findById(Long id);

    List<ClientDTO> findAll();

    void delete(Long id);

    ClientDTO findByEmail(String email);
}