package tn.esprit.autoloc.service;

import java.util.List;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.web.dto.VehiculeDTO;

/**
 * Interface du service pour l'entité Vehicule.
 * Expose les opérations CRUD métier en utilisant des DTO.
 * 
 * Partie 3.4 du TP : Les méthodes reçoivent et retournent des DTO au lieu d'entités JPA.
 */
public interface IVehiculeService {

    /**
     * Créer un nouveau véhicule.
     * 
     * @param dto Le DTO VehiculeDTO à créer
     * @return Le DTO VehiculeDTO créé avec son ID généré
     * @throws IllegalArgumentException si l'id n'est pas null ou si le tarif est négatif
     */
    VehiculeDTO creer(VehiculeDTO dto);

    /**
     * Rechercher un véhicule par son identifiant.
     * 
     * @param id L'identifiant du véhicule
     * @return Le DTO VehiculeDTO trouvé
     * @throws ResourceNotFoundException si le véhicule n'existe pas
     */
    VehiculeDTO obtenirParId(Long id);

    /**
     * Récupérer tous les véhicules.
     * 
     * @return La liste de tous les DTO VehiculeDTO
     */
    List<VehiculeDTO> listerTout();

    /**
     * Mettre à jour un véhicule existant.
     * 
     * @param id L'identifiant du véhicule à modifier
     * @param dto Le DTO VehiculeDTO avec les nouvelles valeurs
     * @return Le DTO VehiculeDTO mis à jour
     * @throws ResourceNotFoundException si le véhicule n'existe pas
     */
    VehiculeDTO modifier(Long id, VehiculeDTO dto);

    /**
     * Supprimer un véhicule par son identifiant.
     * 
     * @param id L'identifiant du véhicule à supprimer
     * @throws ResourceNotFoundException si le véhicule n'existe pas
     */
    void supprimerParId(Long id);
    
    /**
     * Affecte un véhicule à une agence.
     * 
     * @param vehiculeId l'identifiant du véhicule
     * @param agenceId l'identifiant de l'agence
     * @throws ResourceNotFoundException si le véhicule ou l'agence n'existe pas
     */
    void affecterVehiculeAAgence(Long vehiculeId, Long agenceId);
    
    /**
     * Désaffecte un véhicule d'une agence.
     * 
     * @param vehiculeId l'identifiant du véhicule
     * @throws ResourceNotFoundException si le véhicule n'existe pas
     */
    void desaffecterVehiculDeAgence(Long vehiculeId);
}
