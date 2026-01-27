package com.polytech.commandes.service.impl;

import com.polytech.commandes.dto.ClientDTO;
import com.polytech.commandes.entity.Client;
import com.polytech.commandes.exception.ResourceNotFoundException;
import com.polytech.commandes.repository.ClientRepository;
import com.polytech.commandes.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Override
    public ClientDTO create(ClientDTO clientDTO) {
        Client client = Client.builder()
                .nom(clientDTO.getNom())
                .email(clientDTO.getEmail())
                .build();

        Client saved = clientRepository.save(client);
        return toDTO(saved);
    }

    @Override
    public ClientDTO update(Long id, ClientDTO clientDTO) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client non trouvé avec l'ID: " + id));

        client.setNom(clientDTO.getNom());
        client.setEmail(clientDTO.getEmail());

        Client updated = clientRepository.save(client);
        return toDTO(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public ClientDTO findById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client non trouvé avec l'ID: " + id));
        return toDTO(client);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClientDTO> findAll() {
        return clientRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new ResourceNotFoundException("Client non trouvé avec l'ID: " + id);
        }
        clientRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ClientDTO findByEmail(String email) {
        Client client = clientRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Client non trouvé avec l'email: " + email));
        return toDTO(client);
    }

    private ClientDTO toDTO(Client client) {
        return ClientDTO.builder()
                .id(client.getId())
                .nom(client.getNom())
                .email(client.getEmail())
                .build();
    }
}