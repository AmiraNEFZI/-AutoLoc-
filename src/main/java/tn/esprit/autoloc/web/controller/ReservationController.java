package tn.esprit.autoloc.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.service.IReservationService;
import tn.esprit.autoloc.web.dto.ReservationRequestDTO;
import tn.esprit.autoloc.web.dto.ReservationResponseDTO;
import tn.esprit.autoloc.web.validator.ReservationRequestValidator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contrôleur REST pour la gestion des réservations de véhicules.
 * 
 * Partie 3.4 du TP : Les endpoints reçoivent ReservationRequestDTO et retournent ReservationResponseDTO
 * au lieu d'entités JPA directement.
 * 
 * Partie 5.5 du TP : Intégration de la validation avec @Valid et ReservationRequestValidator
 */
@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
@Tag(name = "Reservations", description = "Gestion des réservations de véhicules")
public class ReservationController {
    
    private final IReservationService reservationService;
    private final ReservationRequestValidator reservationRequestValidator;
    
    /**
     * Lister toutes les réservations.
     * 
     * @return ResponseEntity contenant la liste des DTO des réservations avec status 200 OK
     */
    @Operation(summary = "Lister les réservations")
    @ApiResponse(responseCode = "200", description = "Liste des réservations récupérée avec succès")
    @GetMapping
    public ResponseEntity<List<ReservationResponseDTO>> listerReservations() {
        List<ReservationResponseDTO> reservations = reservationService.listerTout();
        return ResponseEntity.ok(reservations);
    }
    
    /**
     * Obtenir une réservation par son identifiant.
     * 
     * @param id L'identifiant de la réservation
     * @return ResponseEntity contenant le DTO de la réservation trouvée avec status 200 OK
     */
    @Operation(summary = "Consulter une réservation")
    @ApiResponse(responseCode = "200", description = "Réservation trouvée")
    @ApiResponse(responseCode = "404", description = "Réservation introuvable")
    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponseDTO> obtenirReservation(
            @Parameter(description = "Identifiant") @PathVariable Long id) {
        ReservationResponseDTO dto = reservationService.obtenirParId(id);
        return ResponseEntity.ok(dto);
    }
    
    /**
     * Créer une nouvelle réservation.
     * 
     * Partie 5.5 : Intégration de @Valid et validation personnalisée via ReservationRequestValidator
     * 
     * @param dto Le DTO ReservationRequestDTO de la réservation à créer
     * @param result Objet BindingResult contenant les résultats de la validation
     * @return ResponseEntity contenant le DTO ReservationResponseDTO de la réservation créée avec status 201 CREATED
     *         ou une Map d'erreurs avec status 400 BAD_REQUEST si la validation échoue
     */
    @Operation(summary = "Créer une réservation")
    @ApiResponse(responseCode = "201", description = "Réservation créée avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    @PostMapping
    public ResponseEntity<?> creerReservation(@Valid @RequestBody ReservationRequestDTO dto, BindingResult result) {
        // Validation personnalisée des dates
        reservationRequestValidator.validate(dto, result);
        
        // Si des erreurs de validation sont détectées
        if (result.hasErrors()) {
            Map<String, String> erreurs = new HashMap<>();
            for (FieldError error : result.getFieldErrors()) {
                erreurs.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(erreurs);
        }
        
        ReservationResponseDTO created = reservationService.creer(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    /**
     * Modifier une réservation existante.
     * 
     * @param id L'identifiant de la réservation à modifier
     * @param dto Le DTO ReservationRequestDTO avec les nouvelles valeurs
     * @return ResponseEntity contenant le DTO ReservationResponseDTO de la réservation modifiée avec status 200 OK
     */
    @Operation(summary = "Modifier une réservation")
    @ApiResponse(responseCode = "200", description = "Réservation modifiée avec succès")
    @ApiResponse(responseCode = "404", description = "Réservation introuvable")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    @PutMapping("/{id}")
    public ResponseEntity<ReservationResponseDTO> modifierReservation(
            @Parameter(description = "Identifiant") @PathVariable Long id,
            @RequestBody ReservationRequestDTO dto) {
        ReservationResponseDTO updated = reservationService.modifier(id, dto);
        return ResponseEntity.ok(updated);
    }
    
    /**
     * Supprimer une réservation par son identifiant.
     * 
     * @param id L'identifiant de la réservation à supprimer
     * @return ResponseEntity avec status 204 NO_CONTENT
     */
    @Operation(summary = "Supprimer une réservation")
    @ApiResponse(responseCode = "204", description = "Réservation supprimée avec succès")
    @ApiResponse(responseCode = "404", description = "Réservation introuvable")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerReservation(
            @Parameter(description = "Identifiant") @PathVariable Long id) {
        reservationService.supprimerParId(id);
        return ResponseEntity.noContent().build();
    }
}
