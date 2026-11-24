package tn.esprit.autoloc.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import tn.esprit.autoloc.domain.enums.ModePaiement;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO pour l'entité Paiement.
 * Attributs : id, montant, datePaiement, modePaiement, contratId
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaiementDTO {

    private Long id;
    
    @NotNull(message = "Le montant est obligatoire")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le montant doit être strictement positif")
    private BigDecimal montant;
    
    @NotNull(message = "La date de paiement est obligatoire")
    private LocalDate datePaiement;
    
    @NotNull(message = "Le mode de paiement est obligatoire")
    private ModePaiement modePaiement;
    
    @NotNull(message = "L'identifiant du contrat est obligatoire")
    private Long contratId;
}
