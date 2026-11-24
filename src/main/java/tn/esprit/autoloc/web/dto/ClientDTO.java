package tn.esprit.autoloc.web.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

/**
 * DTO pour l'entité Client.
 * Attributs : id, nom, prenom, email, telephone, numPermis, dateInscription
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientDTO {

    private Long id;
    
    @NotBlank(message = "Le nom du client est obligatoire")
    private String nom;
    
    @NotBlank(message = "Le prénom du client est obligatoire")
    private String prenom;
    
    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email doit être valide")
    private String email;
    
    @NotBlank(message = "Le téléphone est obligatoire")
    private String telephone;
    
    @NotBlank(message = "Le numéro de permis est obligatoire")
    private String numPermis;
    
    @PastOrPresent(message = "La date d'inscription ne peut pas être dans le futur")
    private LocalDate dateInscription;
}
