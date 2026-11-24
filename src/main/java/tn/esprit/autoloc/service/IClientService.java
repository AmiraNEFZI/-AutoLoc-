package tn.esprit.autoloc.service;

import java.util.List;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.web.dto.ClientDTO;

/**
 * Interface du service pour l'entité Client.
 * Expose les opérations CRUD métier en utilisant des DTO.
 * 
 * Partie 3.4 du TP : Les méthodes reçoivent et retournent des DTO au lieu d'entités JPA.
 */
public interface IClientService {

    /**
     * Créer un nouveau client.
     * 
     * @param dto Le DTO ClientDTO à créer
     * @return Le DTO ClientDTO créé avec son ID généré
     * @throws IllegalArgumentException si l'id n'est pas null ou si un attribut obligatoire est vide
     */
    ClientDTO creer(ClientDTO dto);

    /**
     * Rechercher un client par son identifiant.
     * 
     * @param id L'identifiant du client
     * @return Le DTO ClientDTO trouvé
     * @throws ResourceNotFoundException si le client n'existe pas
     */
    ClientDTO obtenirParId(Long id);

    /**
     * Récupérer tous les clients.
     * 
     * @return La liste de tous les DTO ClientDTO
     */
    List<ClientDTO> listerTout();

    /**
     * Mettre à jour un client existant.
     * 
     * @param id L'identifiant du client à modifier
     * @param dto Le DTO ClientDTO avec les nouvelles valeurs
     * @return Le DTO ClientDTO mis à jour
     * @throws ResourceNotFoundException si le client n'existe pas
     */
    ClientDTO modifier(Long id, ClientDTO dto);

    /**
     * Supprimer un client par son identifiant.
     * 
     * @param id L'identifiant du client à supprimer
     * @throws ResourceNotFoundException si le client n'existe pas
     */
    void supprimerParId(Long id);
}
