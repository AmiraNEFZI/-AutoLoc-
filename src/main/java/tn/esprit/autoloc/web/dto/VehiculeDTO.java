package tn.esprit.autoloc.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import tn.esprit.autoloc.domain.enums.CategorieVehicule;
import tn.esprit.autoloc.domain.enums.StatutVehicule;
import java.math.BigDecimal;

/**
 * DTO pour l'entité Vehicule.
 * Attributs : id, immatriculation, marque, modele, categorie,
 *             tarifJournalier, statut, agenceId
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehiculeDTO {

    private Long id;
    
    @NotBlank(message = "L'immatriculation est obligatoire")
    private String immatriculation;
    
    @NotBlank(message = "La marque est obligatoire")
    private String marque;
    
    @NotBlank(message = "Le modèle est obligatoire")
    private String modele;
    
    @NotNull(message = "La catégorie est obligatoire")
    private CategorieVehicule categorie;
    
    @NotNull(message = "Le tarif journalier est obligatoire")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le tarif journalier doit être strictement positif")
    private BigDecimal tarifJournalier;
    
    private StatutVehicule statut;
    
    @NotNull(message = "L'identifiant de l'agence est obligatoire")
    private Long agenceId;
}
