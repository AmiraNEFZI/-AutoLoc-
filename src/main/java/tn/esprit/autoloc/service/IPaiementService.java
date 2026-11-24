package tn.esprit.autoloc.service;

import java.util.List;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.web.dto.PaiementDTO;

/**
 * Service pour l'entité Paiement.
 * Lecture seule : aucune création, modification ni suppression.
 * Les paiements passent par le Contrat (gestion des liens).
 * 
 * Partie 3.4 du TP : Les méthodes retournent des DTO au lieu d'entités JPA.
 */
public interface IPaiementService {

    /**
     * Rechercher un paiement par son identifiant.
     * 
     * @param id L'identifiant du paiement
     * @return Le DTO PaiementDTO trouvé
     * @throws ResourceNotFoundException si le paiement n'existe pas
     */
    PaiementDTO obtenirParId(Long id);

    /**
     * Récupérer tous les paiements.
     * 
     * @return La liste de tous les DTO PaiementDTO
     */
    List<PaiementDTO> listerTout();
}
