package tn.esprit.autoloc.web.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO pour l'entité Contrat.
 * Attributs : id, dateSignature, montantTotal, valide, reservationId
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContratDTO {

    private Long id;
    
    @NotNull(message = "La date de signature est obligatoire")
    @PastOrPresent(message = "La date de signature ne peut pas être dans le futur")
    private LocalDate dateSignature;
    
    @NotNull(message = "Le montant total est obligatoire")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le montant total doit être positif")
    private BigDecimal montantTotal;
    
    @NotNull(message = "Le statut de validation est obligatoire")
    private Boolean valide;
    
    @NotNull(message = "L'identifiant de la réservation est obligatoire")
    private Long reservationId;
}
