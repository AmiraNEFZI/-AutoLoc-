package tn.esprit.autoloc.service;

import java.util.List;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.exception.ResourceNotFoundException;

/**
 * Interface du service pour l'entité Agence.
 * Expose les opérations CRUD métier.
 */
public interface IAgenceService {

    /**
     * Créer une nouvelle agence.
     * Refuse un id non null.
     * 
     * @param agence L'agence à créer
     * @return L'agence créée avec son ID généré
     * @throws IllegalArgumentException si l'id n'est pas null ou si un attribut obligatoire est vide
     */
    Agence create(Agence agence);

    /**
     * Rechercher une agence par son identifiant.
     * 
     * @param id L'identifiant de l'agence
     * @return L'agence trouvée
     * @throws ResourceNotFoundException si l'agence n'existe pas
     */
    Agence findById(Long id);

    /**
     * Récupérer toutes les agences.
     * 
     * @return La liste de toutes les agences
     */
    List<Agence> findAll();

    /**
     * Mettre à jour une agence existante.
     * 
     * @param id L'identifiant de l'agence à modifier
     * @param agence Les nouvelles valeurs
     * @return L'agence mise à jour
     * @throws ResourceNotFoundException si l'agence n'existe pas
     */
    Agence update(Long id, Agence agence);

    /**
     * Supprimer une agence par son identifiant.
     * 
     * @param id L'identifiant de l'agence à supprimer
     * @throws ResourceNotFoundException si l'agence n'existe pas
     */
    void deleteById(Long id);
}
