package tn.esprit.autoloc.web.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.service.IVehiculeService;
import tn.esprit.autoloc.web.dto.VehiculeDTO;

/**
 * Contrôleur REST pour la gestion des véhicules.
 * 
 * Partie 3.4 du TP : Les endpoints CRUD reçoivent et retournent des DTO (VehiculeDTO)
 * au lieu d'entités JPA directement.
 * Les endpoints d'affectation/désaffectation restent inchangés (Partie 1).
 */
@RestController
@RequestMapping("/vehicules")
@RequiredArgsConstructor
public class VehiculeController {

    private final IVehiculeService vehiculeService;

    /**
     * Créer un nouveau véhicule.
     * 
     * @param dto Le DTO du véhicule à créer
     * @return ResponseEntity contenant le DTO du véhicule créé avec status 201 CREATED
     */
    @PostMapping
    public ResponseEntity<VehiculeDTO> creerVehicule(@RequestBody VehiculeDTO dto) {
        VehiculeDTO created = vehiculeService.creer(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Obtenir un véhicule par son identifiant.
     * 
     * @param id L'identifiant du véhicule
     * @return ResponseEntity contenant le DTO du véhicule trouvé avec status 200 OK
     */
    @GetMapping("/{id}")
    public ResponseEntity<VehiculeDTO> obtenirVehicule(@PathVariable Long id) {
        VehiculeDTO dto = vehiculeService.obtenirParId(id);
        return ResponseEntity.ok(dto);
    }

    /**
     * Lister tous les véhicules.
     * 
     * @return ResponseEntity contenant la liste des DTO des véhicules avec status 200 OK
     */
    @GetMapping
    public ResponseEntity<List<VehiculeDTO>> listerVehicules() {
        List<VehiculeDTO> vehicules = vehiculeService.listerTout();
        return ResponseEntity.ok(vehicules);
    }

    /**
     * Modifier un véhicule existant.
     * 
     * @param id L'identifiant du véhicule à modifier
     * @param dto Le DTO avec les nouvelles valeurs
     * @return ResponseEntity contenant le DTO du véhicule modifié avec status 200 OK
     */
    @PutMapping("/{id}")
    public ResponseEntity<VehiculeDTO> modifierVehicule(
            @PathVariable Long id,
            @RequestBody VehiculeDTO dto) {
        VehiculeDTO updated = vehiculeService.modifier(id, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * Supprimer un véhicule par son identifiant.
     * 
     * @param id L'identifiant du véhicule à supprimer
     * @return ResponseEntity avec status 204 NO_CONTENT
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerVehicule(@PathVariable Long id) {
        vehiculeService.supprimerParId(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Affecte un véhicule à une agence.
     * Endpoint REST PUT selon la logique du TP Partie 1.
     * 
     * @param vehiculeId l'identifiant du véhicule
     * @param agenceId l'identifiant de l'agence
     */
    @PutMapping("/{vehiculeId}/affecter/{agenceId}")
    public ResponseEntity<Void> affecterVehiculeAAgence(
            @PathVariable Long vehiculeId,
            @PathVariable Long agenceId) {
        vehiculeService.affecterVehiculeAAgence(vehiculeId, agenceId);
        return ResponseEntity.ok().build();
    }

    /**
     * Désaffecte un véhicule d'une agence.
     * Endpoint REST PUT selon la logique du TP Partie 1.
     * 
     * @param vehiculeId l'identifiant du véhicule
     */
    @PutMapping("/{vehiculeId}/desaffecter")
    public ResponseEntity<Void> desaffecterVehiculDeAgence(@PathVariable Long vehiculeId) {
        vehiculeService.desaffecterVehiculDeAgence(vehiculeId);
        return ResponseEntity.ok().build();
    }
}
