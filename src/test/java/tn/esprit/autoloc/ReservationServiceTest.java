package tn.esprit.autoloc;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.enums.StatutReservation;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.service.IReservationService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour ReservationService (TP 4 - Couche Service).
 */
@SpringBootTest
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ReservationServiceTest {

    @Autowired
    private IReservationService reservationService;

    private static Long idReservationSauvegardee;

    @Test
    @Order(1)
    public void testCreateReservation() {
        log.info("========== TEST 1: Créer une réservation ==========");

        Reservation reservation = new Reservation();
        reservation.setDateDebut(LocalDate.now());
        reservation.setDateFin(LocalDate.now().plusDays(7));
        reservation.setStatut(StatutReservation.EN_ATTENTE);

        Reservation reservationSauvegardee = reservationService.create(reservation);

        assertNotNull(reservationSauvegardee);
        assertNotNull(reservationSauvegardee.getIdReservation());
        assertEquals(StatutReservation.EN_ATTENTE, reservationSauvegardee.getStatut());

        log.info("✓ Réservation créée avec succès, ID = {}", reservationSauvegardee.getIdReservation());
        idReservationSauvegardee = reservationSauvegardee.getIdReservation();
    }

    @Test
    @Order(2)
    public void testFindAllReservations() {
        log.info("========== TEST 2: Lister toutes les réservations ==========");

        List<Reservation> reservations = reservationService.findAll();

        assertNotNull(reservations);
        assertTrue(reservations.size() > 0);

        log.info("✓ Nombre total de réservations : {}", reservations.size());
    }

    @Test
    @Order(3)
    public void testFindById() {
        log.info("========== TEST 3: Rechercher une réservation par ID ==========");

        Reservation reservation = reservationService.findById(idReservationSauvegardee);

        assertNotNull(reservation);
        assertEquals(StatutReservation.EN_ATTENTE, reservation.getStatut());

        log.info("✓ Réservation trouvée avec statut : {}", reservation.getStatut());
    }

    @Test
    @Order(4)
    public void testUpdateReservation() {
        log.info("========== TEST 4: Mettre à jour une réservation ==========");

        Reservation reservation = reservationService.findById(idReservationSauvegardee);
        reservation.setStatut(StatutReservation.CONFIRMEE);
        reservation.setDateFin(LocalDate.now().plusDays(10));

        Reservation reservationModifiee = reservationService.update(idReservationSauvegardee, reservation);

        assertEquals(StatutReservation.CONFIRMEE, reservationModifiee.getStatut());

        log.info("✓ Réservation mise à jour : statut={}", reservationModifiee.getStatut());
    }

    @Test
    @Order(5)
    public void testDeleteReservation() {
        log.info("========== TEST 5: Supprimer une réservation ==========");

        reservationService.deleteById(idReservationSauvegardee);

        assertThrows(ResourceNotFoundException.class, () -> {
            reservationService.findById(idReservationSauvegardee);
        });

        log.info("✓ Réservation supprimée avec ID : {}", idReservationSauvegardee);
    }

    @Test
    @Order(6)
    public void testCreateReservationAvecDateFinAvantDebut() {
        log.info("========== TEST 6: Refuser création avec dateFin avant dateDebut ==========");

        Reservation reservation = new Reservation();
        reservation.setDateDebut(LocalDate.now());
        reservation.setDateFin(LocalDate.now().minusDays(5)); // Date de fin AVANT date de début
        reservation.setStatut(StatutReservation.EN_ATTENTE);

        assertThrows(IllegalArgumentException.class, () -> {
            reservationService.create(reservation);
        });

        log.info("✓ Règle métier respectée : dateFin ne peut pas être avant dateDebut");
    }
}
