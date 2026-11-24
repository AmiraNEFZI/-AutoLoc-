package tn.esprit.autoloc.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.service.IPaiementService;
import tn.esprit.autoloc.web.dto.PaiementDTO;

import java.util.List;

/**
 * Contrôleur REST pour la consultation des paiements (lecture seule).
 * 
 * Partie 3.4 du TP : Les endpoints retournent des DTO (PaiementDTO)
 * au lieu d'entités JPA directement.
 */
@RestController
@RequestMapping("/paiements")
@RequiredArgsConstructor
@Tag(name = "Paiements", description = "Consultation des paiements (lecture seule)")
public class PaiementController {
    
    private final IPaiementService paiementService;
    
    /**
     * Lister tous les paiements.
     * 
     * @return ResponseEntity contenant la liste des DTO des paiements avec status 200 OK
     */
    @Operation(summary = "Lister les paiements")
    @ApiResponse(responseCode = "200", description = "Liste des paiements récupérée avec succès")
    @GetMapping
    public ResponseEntity<List<PaiementDTO>> listerPaiements() {
        List<PaiementDTO> paiements = paiementService.listerTout();
        return ResponseEntity.ok(paiements);
    }
    
    /**
     * Obtenir un paiement par son identifiant.
     * 
     * @param id L'identifiant du paiement
     * @return ResponseEntity contenant le DTO du paiement trouvé avec status 200 OK
     */
    @Operation(summary = "Consulter un paiement")
    @ApiResponse(responseCode = "200", description = "Paiement trouvé")
    @ApiResponse(responseCode = "404", description = "Paiement introuvable")
    @GetMapping("/{id}")
    public ResponseEntity<PaiementDTO> obtenirPaiement(
            @Parameter(description = "Identifiant") @PathVariable Long id) {
        PaiementDTO dto = paiementService.obtenirParId(id);
        return ResponseEntity.ok(dto);
    }
}
