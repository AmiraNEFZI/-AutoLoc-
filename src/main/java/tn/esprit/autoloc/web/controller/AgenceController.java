package tn.esprit.autoloc.web.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.service.IAgenceService;
import tn.esprit.autoloc.web.dto.AgenceDTO;

/**
 * Contrôleur REST pour la gestion des agences.
 * 
 * Partie 3.3 du TP : Les endpoints reçoivent et retournent des DTO (AgenceDTO)
 * au lieu d'entités JPA directement.
 */
@RestController
@RequestMapping("/agences")
@RequiredArgsConstructor
public class AgenceController {

    private final IAgenceService agenceService;

    /**
     * Créer une nouvelle agence.
     * 
     * @param dto Le DTO de l'agence à créer
     * @return ResponseEntity contenant le DTO de l'agence créée avec status 201 CREATED
     */
    @PostMapping
    public ResponseEntity<AgenceDTO> creerAgence(@RequestBody AgenceDTO dto) {
        AgenceDTO created = agenceService.creer(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Obtenir une agence par son identifiant.
     * 
     * @param id L'identifiant de l'agence
     * @return ResponseEntity contenant le DTO de l'agence trouvée avec status 200 OK
     */
    @GetMapping("/{id}")
    public ResponseEntity<AgenceDTO> obtenirAgence(@PathVariable Long id) {
        AgenceDTO dto = agenceService.obtenirParId(id);
        return ResponseEntity.ok(dto);
    }

    /**
     * Lister toutes les agences.
     * 
     * @return ResponseEntity contenant la liste des DTO des agences avec status 200 OK
     */
    @GetMapping
    public ResponseEntity<List<AgenceDTO>> listerAgences() {
        List<AgenceDTO> agences = agenceService.listerTout();
        return ResponseEntity.ok(agences);
    }

    /**
     * Modifier une agence existante.
     * 
     * @param id L'identifiant de l'agence à modifier
     * @param dto Le DTO avec les nouvelles valeurs
     * @return ResponseEntity contenant le DTO de l'agence modifiée avec status 200 OK
     */
    @PutMapping("/{id}")
    public ResponseEntity<AgenceDTO> modifierAgence(
            @PathVariable Long id,
            @RequestBody AgenceDTO dto) {
        AgenceDTO updated = agenceService.modifier(id, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * Supprimer une agence par son identifiant.
     * 
     * @param id L'identifiant de l'agence à supprimer
     * @return ResponseEntity avec status 204 NO_CONTENT
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerAgence(@PathVariable Long id) {
        agenceService.supprimerParId(id);
        return ResponseEntity.noContent().build();
    }
}
