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
import tn.esprit.autoloc.repository.IReservationRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour ReservationRepository (TP 3 - Couche Repository).
 */
@SpringBootTest
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ReservationRepositoryTest {

    @Autowired
    private IReservationRepository reservationRepository;

    private static Long idReservationSauvegardee;

    @Test
    @Order(1)
    public void testSaveReservation() {
        log.info("========== TEST 1: Save Reservation ==========");

        Reservation reservation = new Reservation();
        reservation.setDateDebut(LocalDate.now());
        reservation.setDateFin(LocalDate.now().plusDays(7));
        reservation.setStatut(StatutReservation.EN_ATTENTE);

        Reservation savedReservation = reservationRepository.save(reservation);

        assertNotNull(savedReservation);
        assertNotNull(savedReservation.getIdReservation());
        assertEquals(StatutReservation.EN_ATTENTE, savedReservation.getStatut());

        log.info("✓ Réservation sauvegardée avec ID: {}", savedReservation.getIdReservation());
        idReservationSauvegardee = savedReservation.getIdReservation();
    }

    @Test
    @Order(2)
    public void testFindAll() {
        log.info("========== TEST 2: FindAll Reservations ==========");

        List<Reservation> reservations = (List<Reservation>) reservationRepository.findAll();

        assertNotNull(reservations);
        assertTrue(reservations.size() > 0);

        log.info("✓ Nombre total de réservations: {}", reservations.size());
    }

    @Test
    @Order(3)
    public void testFindById() {
        log.info("========== TEST 3: FindById Reservation ==========");

        Optional<Reservation> optionalReservation = reservationRepository.findById(idReservationSauvegardee);

        assertTrue(optionalReservation.isPresent());
        assertEquals(StatutReservation.EN_ATTENTE, optionalReservation.get().getStatut());

        log.info("✓ Réservation trouvée avec statut: {}", optionalReservation.get().getStatut());
    }

    @Test
    @Order(4)
    public void testUpdateReservation() {
        log.info("========== TEST 4: Update Reservation ==========");

        Reservation reservation = reservationRepository.findById(idReservationSauvegardee).orElseThrow();
        reservation.setStatut(StatutReservation.CONFIRMEE);
        reservation.setDateFin(LocalDate.now().plusDays(10));

        Reservation updatedReservation = reservationRepository.save(reservation);

        assertEquals(StatutReservation.CONFIRMEE, updatedReservation.getStatut());

        log.info("✓ Réservation mise à jour avec nouveau statut: {}", updatedReservation.getStatut());
    }

    @Test
    @Order(5)
    public void testDeleteReservation() {
        log.info("========== TEST 5: Delete Reservation ==========");

        reservationRepository.deleteById(idReservationSauvegardee);

        Optional<Reservation> deletedReservation = reservationRepository.findById(idReservationSauvegardee);
        assertFalse(deletedReservation.isPresent());

        log.info("✓ Réservation supprimée avec ID: {}", idReservationSauvegardee);
    }

    @Test
    @Order(6)
    public void testCount() {
        log.info("========== TEST 6: Count Reservations ==========");

        long count = reservationRepository.count();

        assertTrue(count >= 0);
        log.info("✓ Nombre de réservations: {}", count);
    }
}
