package tn.esprit.autoloc.web.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.service.IClientService;
import tn.esprit.autoloc.web.dto.ClientDTO;

/**
 * Contrôleur REST pour la gestion des clients.
 * 
 * Partie 3.4 du TP : Les endpoints reçoivent et retournent des DTO (ClientDTO)
 * au lieu d'entités JPA directement.
 */
@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    private final IClientService clientService;

    /**
     * Créer un nouveau client.
     * 
     * @param dto Le DTO du client à créer
     * @return ResponseEntity contenant le DTO du client créé avec status 201 CREATED
     */
    @PostMapping
    public ResponseEntity<ClientDTO> creerClient(@RequestBody ClientDTO dto) {
        ClientDTO created = clientService.creer(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Obtenir un client par son identifiant.
     * 
     * @param id L'identifiant du client
     * @return ResponseEntity contenant le DTO du client trouvé avec status 200 OK
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> obtenirClient(@PathVariable Long id) {
        ClientDTO dto = clientService.obtenirParId(id);
        return ResponseEntity.ok(dto);
    }

    /**
     * Lister tous les clients.
     * 
     * @return ResponseEntity contenant la liste des DTO des clients avec status 200 OK
     */
    @GetMapping
    public ResponseEntity<List<ClientDTO>> listerClients() {
        List<ClientDTO> clients = clientService.listerTout();
        return ResponseEntity.ok(clients);
    }

    /**
     * Modifier un client existant.
     * 
     * @param id L'identifiant du client à modifier
     * @param dto Le DTO avec les nouvelles valeurs
     * @return ResponseEntity contenant le DTO du client modifié avec status 200 OK
     */
    @PutMapping("/{id}")
    public ResponseEntity<ClientDTO> modifierClient(
            @PathVariable Long id,
            @RequestBody ClientDTO dto) {
        ClientDTO updated = clientService.modifier(id, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * Supprimer un client par son identifiant.
     * 
     * @param id L'identifiant du client à supprimer
     * @return ResponseEntity avec status 204 NO_CONTENT
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerClient(@PathVariable Long id) {
        clientService.supprimerParId(id);
        return ResponseEntity.noContent().build();
    }
}
