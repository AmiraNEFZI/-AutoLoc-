package tn.esprit.autoloc.service;

import java.util.List;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.web.dto.AgenceDTO;

/**
 * Interface du service pour l'entité Agence.
 * Expose les opérations CRUD métier en utilisant des DTO.
 * 
 * Partie 3 du TP : Les méthodes reçoivent et retournent des DTO au lieu d'entités JPA.
 */
public interface IAgenceService {

    /**
     * Créer une nouvelle agence.
     * 
     * @param dto Le DTO AgenceDTO à créer
     * @return Le DTO AgenceDTO créé avec son ID généré
     * @throws IllegalArgumentException si l'id n'est pas null ou si un attribut obligatoire est vide
     */
    AgenceDTO creer(AgenceDTO dto);

    /**
     * Rechercher une agence par son identifiant.
     * 
     * @param id L'identifiant de l'agence
     * @return Le DTO AgenceDTO trouvé
     * @throws ResourceNotFoundException si l'agence n'existe pas
     */
    AgenceDTO obtenirParId(Long id);

    /**
     * Récupérer toutes les agences.
     * 
     * @return La liste de tous les DTO AgenceDTO
     */
    List<AgenceDTO> listerTout();
    
    /**
     * Mettre à jour une agence existante.
     * 
     * @param id L'identifiant de l'agence à modifier
     * @param dto Le DTO AgenceDTO avec les nouvelles valeurs
     * @return Le DTO AgenceDTO mis à jour
     * @throws ResourceNotFoundException si l'agence n'existe pas
     */
    AgenceDTO modifier(Long id, AgenceDTO dto);

    /**
     * Supprimer une agence par son identifiant.
     * 
     * @param id L'identifiant de l'agence à supprimer
     * @throws ResourceNotFoundException si l'agence n'existe pas
     */
    void supprimerParId(Long id);
}
