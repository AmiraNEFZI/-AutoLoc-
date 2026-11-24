package tn.esprit.autoloc.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * DTO pour l'entité Agence.
 * Attributs : id, nom, ville, adresse, telephone
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgenceDTO {

    private Long id;
    
    @NotBlank(message = "Le nom de l'agence est obligatoire")
    private String nom;
    
    @NotBlank(message = "La ville est obligatoire")
    private String ville;
    
    @NotBlank(message = "L'adresse est obligatoire")
    private String adresse;
    
    @NotBlank(message = "Le téléphone est obligatoire")
    private String telephone;
}
