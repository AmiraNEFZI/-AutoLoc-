package tn.esprit.autoloc.service;

import java.util.List;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.exception.ResourceNotFoundException;

/**
 * Service pour l'entité Paiement.
 * Lecture seule : aucune création, modification ni suppression.
 * Les paiements passent par le Contrat (gestion des liens).
 */
public interface IPaiementService {

    /**
     * Rechercher un paiement par son identifiant.
     * 
     * @param id L'identifiant du paiement
     * @return Le paiement trouvé
     * @throws ResourceNotFoundException si le paiement n'existe pas
     */
    Paiement findById(Long id);

    /**
     * Récupérer tous les paiements.
     * 
     * @return La liste de tous les paiements
     */
    List<Paiement> findAll();
}
