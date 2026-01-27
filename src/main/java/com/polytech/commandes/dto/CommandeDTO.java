package com.polytech.commandes.dto;

import com.polytech.commandes.entity.CommandeStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommandeDTO {

    private Long id;

    private LocalDateTime dateCommande;

    private CommandeStatus status;

    @NotNull(message = "L'ID du client est obligatoire")
    private Long clientId;

    private String clientNom;

    @NotEmpty(message = "La commande doit contenir au moins une ligne")
    @Valid
    @Builder.Default
    private List<LigneCommandeDTO> lignes = new ArrayList<>();

    private BigDecimal montantTotal;
}