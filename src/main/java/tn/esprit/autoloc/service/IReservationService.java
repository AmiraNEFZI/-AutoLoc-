package tn.esprit.autoloc.service;

import java.util.List;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.web.dto.ReservationRequestDTO;
import tn.esprit.autoloc.web.dto.ReservationResponseDTO;

/**
 * Interface du service pour l'entité Reservation.
 * Expose les opérations CRUD métier en utilisant des DTO.
 * 
 * Partie 3.4 du TP : Les méthodes reçoivent ReservationRequestDTO et retournent ReservationResponseDTO.
 */
public interface IReservationService {

    /**
     * Créer une nouvelle réservation.
     * 
     * @param dto Le DTO ReservationRequestDTO à créer (avec clientId, vehiculeId, dates)
     * @return Le DTO ReservationResponseDTO créé avec son ID généré
     * @throws IllegalArgumentException si l'id n'est pas null ou si dateFin < dateDebut
     */
    ReservationResponseDTO creer(ReservationRequestDTO dto);

    /**
     * Rechercher une réservation par son identifiant.
     * 
     * @param id L'identifiant de la réservation
     * @return Le DTO ReservationResponseDTO trouvé
     * @throws ResourceNotFoundException si la réservation n'existe pas
     */
    ReservationResponseDTO obtenirParId(Long id);

    /**
     * Récupérer toutes les réservations.
     * 
     * @return La liste de tous les DTO ReservationResponseDTO
     */
    List<ReservationResponseDTO> listerTout();

    /**
     * Mettre à jour une réservation existante.
     * 
     * @param id L'identifiant de la réservation à modifier
     * @param dto Le DTO ReservationRequestDTO avec les nouvelles valeurs
     * @return Le DTO ReservationResponseDTO mis à jour
     * @throws ResourceNotFoundException si la réservation n'existe pas
     */
    ReservationResponseDTO modifier(Long id, ReservationRequestDTO dto);

    /**
     * Supprimer une réservation par son identifiant.
     * 
     * @param id L'identifiant de la réservation à supprimer
     * @throws ResourceNotFoundException si la réservation n'existe pas
     */
    void supprimerParId(Long id);
}
