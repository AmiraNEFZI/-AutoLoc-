package tn.esprit.autoloc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.domain.Reservation;

/**
 * Repository pour l'entité Reservation.
 * Étend JpaRepository pour avoir toutes les fonctionnalités.
 */
public interface IReservationRepository extends JpaRepository<Reservation, Long> {
}
