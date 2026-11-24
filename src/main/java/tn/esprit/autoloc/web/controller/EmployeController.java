package tn.esprit.autoloc.web.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.service.IEmployeService;
import tn.esprit.autoloc.web.dto.EmployeDTO;

/**
 * Contrôleur REST pour la gestion des employés.
 * 
 * Les endpoints CRUD reçoivent et retournent des DTO (EmployeDTO)
 * au lieu d'entités JPA directement.
 * Les endpoints d'affectation/désaffectation restent inchangés (Partie 1).
 */
@RestController
@RequestMapping("/employes")
@RequiredArgsConstructor
public class EmployeController {

    private final IEmployeService employeService;

    /**
     * Créer un nouvel employé.
     * 
     * @param dto Le DTO de l'employé à créer
     * @return ResponseEntity contenant le DTO de l'employé créé avec status 201 CREATED
     */
    @PostMapping
    public ResponseEntity<EmployeDTO> creerEmploye(@RequestBody EmployeDTO dto) {
        EmployeDTO created = employeService.creer(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Obtenir un employé par son identifiant.
     * 
     * @param id L'identifiant de l'employé
     * @return ResponseEntity contenant le DTO de l'employé trouvé avec status 200 OK
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmployeDTO> obtenirEmploye(@PathVariable Long id) {
        EmployeDTO dto = employeService.obtenirParId(id);
        return ResponseEntity.ok(dto);
    }

    /**
     * Lister tous les employés.
     * 
     * @return ResponseEntity contenant la liste des DTO des employés avec status 200 OK
     */
    @GetMapping
    public ResponseEntity<List<EmployeDTO>> listerEmployes() {
        List<EmployeDTO> employes = employeService.listerTout();
        return ResponseEntity.ok(employes);
    }

    /**
     * Modifier un employé existant.
     * 
     * @param id L'identifiant de l'employé à modifier
     * @param dto Le DTO avec les nouvelles valeurs
     * @return ResponseEntity contenant le DTO de l'employé modifié avec status 200 OK
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmployeDTO> modifierEmploye(
            @PathVariable Long id,
            @RequestBody EmployeDTO dto) {
        EmployeDTO updated = employeService.modifier(id, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * Supprimer un employé par son identifiant.
     * 
     * @param id L'identifiant de l'employé à supprimer
     * @return ResponseEntity avec status 204 NO_CONTENT
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerEmploye(@PathVariable Long id) {
        employeService.supprimerParId(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Affecte un employé à une agence.
     * Endpoint REST PUT selon la logique du TP Partie 1.
     * 
     * @param employeId l'identifiant de l'employé
     * @param agenceId l'identifiant de l'agence
     */
    @PutMapping("/{employeId}/affecter/{agenceId}")
    public ResponseEntity<Void> affecterEmployeAAgence(
            @PathVariable Long employeId,
            @PathVariable Long agenceId) {
        employeService.affecterEmployeAAgence(employeId, agenceId);
        return ResponseEntity.ok().build();
    }

    /**
     * Désaffecte un employé d'une agence.
     * Endpoint REST PUT selon la logique du TP Partie 1.
     * 
     * @param employeId l'identifiant de l'employé
     */
    @PutMapping("/{employeId}/desaffecter")
    public ResponseEntity<Void> desaffecterEmployeDeAgence(@PathVariable Long employeId) {
        employeService.desaffecterEmployeDeAgence(employeId);
        return ResponseEntity.ok().build();
    }
}
