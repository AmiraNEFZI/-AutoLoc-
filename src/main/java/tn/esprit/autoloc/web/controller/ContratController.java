package tn.esprit.autoloc.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.service.IContratService;
import tn.esprit.autoloc.web.dto.ContratDTO;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des contrats.
 * 
 * Partie 3.4 du TP : Les endpoints reçoivent et retournent des DTO (ContratDTO)
 * au lieu d'entités JPA directement.
 */
@RestController
@RequestMapping("/contrats")
@RequiredArgsConstructor
@Tag(name = "Contrats", description = "Gestion des contrats de location")
public class ContratController {
    
    private final IContratService contratService;
    
    /**
     * Lister tous les contrats.
     * 
     * @return ResponseEntity contenant la liste des DTO des contrats avec status 200 OK
     */
    @Operation(summary = "Lister les contrats")
    @ApiResponse(responseCode = "200", description = "Liste des contrats récupérée avec succès")
    @GetMapping
    public ResponseEntity<List<ContratDTO>> listerContrats() {
        List<ContratDTO> contrats = contratService.listerTout();
        return ResponseEntity.ok(contrats);
    }
    
    /**
     * Obtenir un contrat par son identifiant.
     * 
     * @param id L'identifiant du contrat
     * @return ResponseEntity contenant le DTO du contrat trouvé avec status 200 OK
     */
    @Operation(summary = "Consulter un contrat")
    @ApiResponse(responseCode = "200", description = "Contrat trouvé")
    @ApiResponse(responseCode = "404", description = "Contrat introuvable")
    @GetMapping("/{id}")
    public ResponseEntity<ContratDTO> obtenirContrat(
            @Parameter(description = "Identifiant") @PathVariable Long id) {
        ContratDTO dto = contratService.obtenirParId(id);
        return ResponseEntity.ok(dto);
    }
    
    /**
     * Créer un nouveau contrat.
     * 
     * @param dto Le DTO du contrat à créer
     * @return ResponseEntity contenant le DTO du contrat créé avec status 201 CREATED
     */
    @Operation(summary = "Créer un contrat")
    @ApiResponse(responseCode = "201", description = "Contrat créé avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    @PostMapping
    public ResponseEntity<ContratDTO> creerContrat(@RequestBody ContratDTO dto) {
        ContratDTO created = contratService.creer(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    /**
     * Modifier un contrat existant.
     * 
     * @param id L'identifiant du contrat à modifier
     * @param dto Le DTO avec les nouvelles valeurs
     * @return ResponseEntity contenant le DTO du contrat modifié avec status 200 OK
     */
    @Operation(summary = "Modifier un contrat")
    @ApiResponse(responseCode = "200", description = "Contrat modifié avec succès")
    @ApiResponse(responseCode = "404", description = "Contrat introuvable")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    @PutMapping("/{id}")
    public ResponseEntity<ContratDTO> modifierContrat(
            @Parameter(description = "Identifiant") @PathVariable Long id,
            @RequestBody ContratDTO dto) {
        ContratDTO updated = contratService.modifier(id, dto);
        return ResponseEntity.ok(updated);
    }
    
    /**
     * Supprimer un contrat par son identifiant.
     * 
     * @param id L'identifiant du contrat à supprimer
     * @return ResponseEntity avec status 204 NO_CONTENT
     */
    @Operation(summary = "Supprimer un contrat")
    @ApiResponse(responseCode = "204", description = "Contrat supprimé avec succès")
    @ApiResponse(responseCode = "404", description = "Contrat introuvable")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerContrat(
            @Parameter(description = "Identifiant") @PathVariable Long id) {
        contratService.supprimerParId(id);
        return ResponseEntity.noContent().build();
    }
}
