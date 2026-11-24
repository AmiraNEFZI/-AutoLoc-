package tn.esprit.autoloc.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import tn.esprit.autoloc.domain.enums.RoleEmploye;

/**
 * DTO pour l'entité Employe.
 * Utilisé pour les échanges avec les clients REST (API).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeDTO {
    
    private Long id;
    
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;
    
    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;
    
    @NotNull(message = "Le rôle est obligatoire")
    private RoleEmploye role;
    
    private Long agenceId; // ID de l'agence à laquelle l'employé est affecté
}
