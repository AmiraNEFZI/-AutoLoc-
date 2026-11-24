package tn.esprit.autoloc.service.impl;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.domain.enums.StatutReservation;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IClientRepository;
import tn.esprit.autoloc.repository.IReservationRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;
import tn.esprit.autoloc.service.IReservationService;
import tn.esprit.autoloc.service.observer.ReservationSubject;
import tn.esprit.autoloc.web.dto.ReservationRequestDTO;
import tn.esprit.autoloc.web.dto.ReservationResponseDTO;
import tn.esprit.autoloc.web.mapper.ReservationMapper;

/**
 * Implémentation du service Reservation utilisant des DTO.
 * 
 * Partie 3.4 du TP : Le service injecte le repository et le mapper correspondant.
 * Pattern : ReservationRequestDTO (entrée) -> Entité -> ReservationResponseDTO (sortie).
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ReservationServiceImpl implements IReservationService {

    private final IReservationRepository reservationRepository;
    private final IClientRepository clientRepository;
    private final IVehiculeRepository vehiculeRepository;
    private final ReservationMapper reservationMapper;
    private final ReservationSubject reservationSubject;

    @Override
    public ReservationResponseDTO creer(ReservationRequestDTO dto) {
        // Validation métier : dates
        if (dto.getDateDebut() != null && dto.getDateFin() != null &&
            dto.getDateFin().isBefore(dto.getDateDebut())) {
            throw new IllegalArgumentException(
                    "La date de fin ne peut pas être antérieure à la date de début");
        }
        
        // Rechercher le client
        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client", dto.getClientId()));
        
        // Rechercher le véhicule
        Vehicule vehicule = vehiculeRepository.findById(dto.getVehiculeId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicule", dto.getVehiculeId()));
        
        // Créer l'entité Reservation
        Reservation reservation = Reservation.builder()
                .client(client)
                .vehicule(vehicule)
                .dateDebut(dto.getDateDebut())
                .dateFin(dto.getDateFin())
                .statut(StatutReservation.EN_ATTENTE) // Statut par défaut
                .build();
        
        // Enregistrer l'entité
        Reservation saved = reservationRepository.save(reservation);
        
        // Convertir résultat en DTO
        return reservationMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ReservationResponseDTO obtenirParId(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", id));
        return reservationMapper.toDto(reservation);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationResponseDTO> listerTout() {
        return reservationRepository.findAll().stream()
                .map(reservationMapper::toDto)
                .toList();
    }

    @Override
    public ReservationResponseDTO modifier(Long id, ReservationRequestDTO dto) {
        // Rechercher l'entité existante
        Reservation existant = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", id));
        
        // Mettre à jour les attributs
        existant.setDateDebut(dto.getDateDebut());
        existant.setDateFin(dto.getDateFin());
        
        // Mettre à jour les relations si nécessaire
        if (dto.getClientId() != null && !dto.getClientId().equals(existant.getClient().getIdClient())) {
            Client client = clientRepository.findById(dto.getClientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Client", dto.getClientId()));
            existant.setClient(client);
        }
        
        if (dto.getVehiculeId() != null && !dto.getVehiculeId().equals(existant.getVehicule().getIdVehicule())) {
            Vehicule vehicule = vehiculeRepository.findById(dto.getVehiculeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Vehicule", dto.getVehiculeId()));
            existant.setVehicule(vehicule);
        }
        
        // Enregistrer et convertir en DTO
        Reservation saved = reservationRepository.save(existant);
        return reservationMapper.toDto(saved);
    }

    @Override
    public void supprimerParId(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Reservation", id);
        }
        reservationRepository.deleteById(id);
    }
    
    /**
     * Crée une réservation et notifie tous les observateurs.
     * 
     * Pattern Observer utilisé : Quand une réservation est créée avec statut CONFIRMEE,
     * tous les observateurs (Email, SMS, etc.) sont automatiquement notifiés.
     * 
     * @param client le client qui réserve
     * @return la réservation créée
     */
    public Reservation creerReservation(Client client) {
        // 1. Créer la réservation
        Reservation reservation = Reservation.builder()
                .client(client)
                .dateDebut(LocalDate.now())
                .dateFin(LocalDate.now().plusDays(3))
                .statut(StatutReservation.CONFIRMEE)
                .build();
        
        // 2. Sauvegarder en base
        reservation = reservationRepository.save(reservation);
        
        // 3. Déclencher le processus métier via le pattern Observer
        // Tous les observateurs inscrits (Email, SMS, etc.) seront notifiés automatiquement
        reservationSubject.notifierConfirmation(reservation);
        
        return reservation;
    }
    
    /**
     * Annule une réservation existante et notifie tous les observateurs.
     * 
     * Pattern Observer utilisé : Quand une réservation est annulée,
     * tous les observateurs sont automatiquement notifiés.
     * 
     * @param reservation la réservation à annuler
     * @return la réservation annulée
     */
    public Reservation annulerReservation(Reservation reservation) {
        // 1. Changer le statut
        reservation.setStatut(StatutReservation.ANNULEE);
        
        // 2. Log de l'annulation
        log.info("Réservation {} annulée pour le client {}", 
                reservation.getIdReservation(), 
                reservation.getClient().getNom());
        
        // 3. Déclencher le processus métier via le pattern Observer
        // Tous les observateurs inscrits (Email, SMS, etc.) seront notifiés automatiquement
        reservationSubject.notifierAnnulation(reservation);
        
        return reservation;
    }
}
