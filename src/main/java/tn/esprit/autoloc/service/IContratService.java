package tn.esprit.autoloc.service;

import java.util.List;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.web.dto.ContratDTO;

/**
 * Interface du service pour l'entité Contrat.
 * Expose les opérations CRUD métier en utilisant des DTO.
 * 
 * Partie 3.4 du TP : Les méthodes reçoivent et retournent des DTO au lieu d'entités JPA.
 */
public interface IContratService {

    /**
     * Créer un nouveau contrat.
     * 
     * @param dto Le DTO ContratDTO à créer
     * @return Le DTO ContratDTO créé avec son ID généré
     * @throws IllegalArgumentException si l'id n'est pas null
     */
    ContratDTO creer(ContratDTO dto);

    /**
     * Rechercher un contrat par son identifiant.
     * 
     * @param id L'identifiant du contrat
     * @return Le DTO ContratDTO trouvé
     * @throws ResourceNotFoundException si le contrat n'existe pas
     */
    ContratDTO obtenirParId(Long id);

    /**
     * Récupérer tous les contrats.
     * 
     * @return La liste de tous les DTO ContratDTO
     */
    List<ContratDTO> listerTout();

    /**
     * Mettre à jour un contrat existant.
     * 
     * @param id L'identifiant du contrat à modifier
     * @param dto Le DTO ContratDTO avec les nouvelles valeurs
     * @return Le DTO ContratDTO mis à jour
     * @throws ResourceNotFoundException si le contrat n'existe pas
     */
    ContratDTO modifier(Long id, ContratDTO dto);

    /**
     * Supprimer un contrat par son identifiant.
     * 
     * @param id L'identifiant du contrat à supprimer
     * @throws ResourceNotFoundException si le contrat n'existe pas
     */
    void supprimerParId(Long id);
}