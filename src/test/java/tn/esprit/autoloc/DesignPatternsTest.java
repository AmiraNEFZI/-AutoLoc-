package tn.esprit.autoloc;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.domain.enums.CategorieVehicule;
import tn.esprit.autoloc.domain.enums.StatutReservation;
import tn.esprit.autoloc.repository.IClientRepository;
import tn.esprit.autoloc.service.factory.NotificationFactory;
import tn.esprit.autoloc.service.factory.TarificationStrategyFactory;
import tn.esprit.autoloc.service.impl.ReservationServiceImpl;
import tn.esprit.autoloc.service.observer.EmailNotificationObserver;
import tn.esprit.autoloc.service.observer.IReservationObserver;
import tn.esprit.autoloc.service.observer.ReservationSubject;
import tn.esprit.autoloc.service.observer.SmsNotificationObserver;
import tn.esprit.autoloc.service.strategy.ITarificationStrategy;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour les Design Patterns GOF (Atelier 6).
 * 
 * Ce fichier teste :
 * - STRATEGY PATTERN : Calcul de tarification dynamique
 * - FACTORY METHOD : Création d'objets (stratégies, observateurs)
 * - OBSERVER PATTERN : Notification automatique lors des événements
 */
@SpringBootTest
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DesignPatternsTest {

    @Autowired
    private TarificationStrategyFactory tarificationFactory;

    @Autowired
    private NotificationFactory notificationFactory;

    @Autowired
    private ReservationSubject reservationSubject;

    @Autowired
    private EmailNotificationObserver emailObserver;

    @Autowired
    private SmsNotificationObserver smsObserver;

    @Autowired
    private ReservationServiceImpl reservationService;
    
    @Autowired
    private IClientRepository clientRepository;

    // ==========================================
    // TESTS : FACTORY METHOD PATTERN
    // ==========================================

    @Test
    @Order(1)
    @DisplayName("Factory - Création observateur Email")
    void testNotificationFactoryCreerEmail() {
        log.info("========== TEST 1: NotificationFactory - Créer observateur Email ==========");

        IReservationObserver observer = notificationFactory.creerObservateur("email");

        assertNotNull(observer, "L'observateur ne doit pas être null");
        assertInstanceOf(EmailNotificationObserver.class, observer, "Doit retourner EmailNotificationObserver");

        log.info("✓ Factory a créé correctement un EmailNotificationObserver");
    }

    @Test
    @Order(2)
    @DisplayName("Factory - Création observateur SMS")
    void testNotificationFactoryCreerSms() {
        log.info("========== TEST 2: NotificationFactory - Créer observateur SMS ==========");

        IReservationObserver observer = notificationFactory.creerObservateur("sms");

        assertNotNull(observer, "L'observateur ne doit pas être null");
        assertInstanceOf(SmsNotificationObserver.class, observer, "Doit retourner SmsNotificationObserver");

        log.info("✓ Factory a créé correctement un SmsNotificationObserver");
    }

    @Test
    @Order(3)
    @DisplayName("Factory - Canal inconnu lance exception")
    void testNotificationFactoryCanalInconnu() {
        log.info("========== TEST 3: NotificationFactory - Canal inconnu ==========");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            notificationFactory.creerObservateur("whatsapp");
        });

        assertTrue(exception.getMessage().contains("Canal de notification inconnu"),
                "Le message d'erreur doit indiquer un canal inconnu");

        log.info("✓ Factory rejette correctement un canal inconnu : {}", exception.getMessage());
    }

    @Test
    @Order(4)
    @DisplayName("Factory - Canal null lance exception")
    void testNotificationFactoryCanalNull() {
        log.info("========== TEST 4: NotificationFactory - Canal null ==========");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            notificationFactory.creerObservateur(null);
        });

        assertTrue(exception.getMessage().contains("Canal de notification inconnu"),
                "Le message d'erreur doit gérer le cas null");

        log.info("✓ Factory rejette correctement un canal null");
    }

    // ==========================================
    // TESTS : STRATEGY PATTERN + FACTORY
    // ==========================================

    @Test
    @Order(5)
    @DisplayName("Strategy - Tarif Standard (4 jours en semaine)")
    void testTarifStandard() {
        log.info("========== TEST 5: Strategy Pattern - Tarif Standard ==========");

        // Réservation de 4 jours commençant un lundi (ni weekend, ni longue durée)
        LocalDate lundi = LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));
        Reservation reservation = creerReservation(lundi, lundi.plusDays(4));
        Vehicule vehicule = creerVehicule(CategorieVehicule.CITADINE);

        ITarificationStrategy strategy = tarificationFactory.resoudre(reservation);
        BigDecimal prix = strategy.calculerTarif(vehicule, reservation);

        assertEquals("STANDARD", strategy.getCode(), "Doit utiliser la stratégie STANDARD");
        // 4 jours * 150€ = 600€
        assertEquals(0, new BigDecimal("600.00").compareTo(prix), "Prix doit être 4 * 150€ = 600€");

        log.info("✓ Stratégie STANDARD appliquée : {} jours → {}€", 
                 reservation.getDateFin().toEpochDay() - reservation.getDateDebut().toEpochDay(), 
                 prix);
    }

    @Test
    @Order(6)
    @DisplayName("Strategy - Tarif Weekend (3 jours commençant vendredi)")
    void testTarifWeekend() {
        log.info("========== TEST 6: Strategy Pattern - Tarif Weekend ==========");

        // Réservation de 3 jours commençant un vendredi (durée ≤ 3j ET weekend)
        LocalDate vendredi = LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY));
        Reservation reservation = creerReservation(vendredi, vendredi.plusDays(3));
        Vehicule vehicule = creerVehicule(CategorieVehicule.BERLINE);

        ITarificationStrategy strategy = tarificationFactory.resoudre(reservation);
        BigDecimal prix = strategy.calculerTarif(vehicule, reservation);

        assertEquals("WEEKEND", strategy.getCode(), "Doit utiliser la stratégie WEEKEND");
        // 3 jours * 100€ = 300€
        assertEquals(0, new BigDecimal("300.00").compareTo(prix), "Prix doit être 3 * 100€ = 300€");

        log.info("✓ Stratégie WEEKEND appliquée : {} jours → {}€", 
                 reservation.getDateFin().toEpochDay() - reservation.getDateDebut().toEpochDay(), 
                 prix);
    }

    @Test
    @Order(7)
    @DisplayName("Strategy - Tarif Longue Durée (7+ jours)")
    void testTarifLongueDuree() {
        log.info("========== TEST 7: Strategy Pattern - Tarif Longue Durée ==========");

        // Réservation de 10 jours (durée ≥ 7j)
        LocalDate debut = LocalDate.now();
        Reservation reservation = creerReservation(debut, debut.plusDays(10));
        Vehicule vehicule = creerVehicule(CategorieVehicule.SUV);

        ITarificationStrategy strategy = tarificationFactory.resoudre(reservation);
        BigDecimal prix = strategy.calculerTarif(vehicule, reservation);

        assertEquals("LONGUE_DUREE", strategy.getCode(), "Doit utiliser la stratégie LONGUE_DUREE");
        // 10 jours * 80€ = 800€
        assertEquals(0, new BigDecimal("800.00").compareTo(prix), "Prix doit être 10 * 80€ = 800€");

        log.info("✓ Stratégie LONGUE_DUREE appliquée : {} jours → {}€", 
                 reservation.getDateFin().toEpochDay() - reservation.getDateDebut().toEpochDay(), 
                 prix);
    }

    @Test
    @Order(8)
    @DisplayName("Strategy - Cas limite : exactement 7 jours → Longue Durée")
    void testTarifLongueDuree7Jours() {
        log.info("========== TEST 8: Strategy Pattern - Cas limite 7 jours ==========");

        LocalDate debut = LocalDate.now();
        Reservation reservation = creerReservation(debut, debut.plusDays(7));

        ITarificationStrategy strategy = tarificationFactory.resoudre(reservation);

        assertEquals("LONGUE_DUREE", strategy.getCode(), 
                    "Avec durée = 7j, doit appliquer LONGUE_DUREE (seuil ≥ 7)");

        log.info("✓ Cas limite 7 jours → Stratégie LONGUE_DUREE");
    }

    // ==========================================
    // TESTS : OBSERVER PATTERN
    // ==========================================

    @Test
    @Order(9)
    @DisplayName("Observer - Notification confirmation réservation")
    void testObserverConfirmation() {
        log.info("========== TEST 9: Observer Pattern - Confirmation réservation ==========");

        Client client = creerClient("Jean Dupont", "jean@email.com", "0612345678");
        Reservation reservation = Reservation.builder()
                .client(client)
                .dateDebut(LocalDate.now())
                .dateFin(LocalDate.now().plusDays(3))
                .statut(StatutReservation.CONFIRMEE)
                .build();

        // Notification de confirmation (déclenche tous les observateurs)
        assertDoesNotThrow(() -> {
            reservationSubject.notifierConfirmation(reservation);
        });

        log.info("✓ Notification de confirmation envoyée à tous les observateurs (Email + SMS)");
        log.info("  → Vérifier les logs : doit contenir [EMAIL] et [SMS]");
    }

    @Test
    @Order(10)
    @DisplayName("Observer - Notification annulation réservation")
    void testObserverAnnulation() {
        log.info("========== TEST 10: Observer Pattern - Annulation réservation ==========");

        Client client = creerClient("Marie Martin", "marie@email.com", "0698765432");
        Reservation reservation = Reservation.builder()
                .client(client)
                .dateDebut(LocalDate.now())
                .dateFin(LocalDate.now().plusDays(5))
                .statut(StatutReservation.ANNULEE)
                .build();

        // Notification d'annulation (déclenche tous les observateurs)
        assertDoesNotThrow(() -> {
            reservationSubject.notifierAnnulation(reservation);
        });

        log.info("✓ Notification d'annulation envoyée à tous les observateurs (Email + SMS)");
        log.info("  → Vérifier les logs : doit contenir [EMAIL] et [SMS]");
    }

    @Test
    @Order(11)
    @DisplayName("Observer - Intégration complète avec ReservationService")
    void testObserverIntegrationCreerReservation() {
        log.info("========== TEST 11: Observer - Intégration avec ReservationService ==========");

        // IMPORTANT : Le client doit être sauvegardé en base AVANT de créer la réservation
        Client client = creerClient("Alice Bernard", "alice@email.com", "0645678901");
        client = clientRepository.save(client);  // Sauvegarde en base
        
        // Maintenant on peut créer la réservation avec ce client persisté
        final Client clientPersiste = client;  // Pour utiliser dans lambda
        
        // Créer une réservation via le service (doit déclencher automatiquement les observers)
        Reservation reservation = assertDoesNotThrow(() -> {
            return reservationService.creerReservation(clientPersiste);
        });

        assertNotNull(reservation, "La réservation ne doit pas être null");
        assertNotNull(reservation.getIdReservation(), "La réservation doit avoir un ID");
        assertEquals(StatutReservation.CONFIRMEE, reservation.getStatut(), 
                    "Le statut doit être CONFIRMEE");

        log.info("✓ Réservation créée avec ID={} et observers notifiés automatiquement", 
                 reservation.getIdReservation());
        log.info("  → Pattern Observer fonctionne : création → notification automatique");
    }

    @Test
    @Order(12)
    @DisplayName("Observer - Intégration annulation avec ReservationService")
    void testObserverIntegrationAnnulerReservation() {
        log.info("========== TEST 12: Observer - Intégration annulation avec ReservationService ==========");

        // IMPORTANT : Le client doit être sauvegardé en base AVANT de créer la réservation
        Client client = creerClient("Bob Durand", "bob@email.com", "0656789012");
        client = clientRepository.save(client);  // Sauvegarde en base
        
        Reservation reservation = Reservation.builder()
                .client(client)
                .dateDebut(LocalDate.now())
                .dateFin(LocalDate.now().plusDays(2))
                .statut(StatutReservation.CONFIRMEE)
                .build();

        // Annuler la réservation (doit déclencher automatiquement les observers)
        Reservation reservationAnnulee = assertDoesNotThrow(() -> {
            return reservationService.annulerReservation(reservation);
        });

        assertEquals(StatutReservation.ANNULEE, reservationAnnulee.getStatut(), 
                    "Le statut doit être ANNULEE");

        log.info("✓ Réservation annulée et observers notifiés automatiquement");
        log.info("  → Pattern Observer fonctionne : annulation → notification automatique");
    }

    // ==========================================
    // MÉTHODES UTILITAIRES
    // ==========================================

    /**
     * Crée une réservation de test.
     */
    private Reservation creerReservation(LocalDate debut, LocalDate fin) {
        return Reservation.builder()
                .dateDebut(debut)
                .dateFin(fin)
                .statut(StatutReservation.EN_ATTENTE)
                .build();
    }

    /**
     * Crée un véhicule de test.
     */
    private Vehicule creerVehicule(CategorieVehicule categorie) {
        return Vehicule.builder()
                .immatriculation("TEST-123")
                .categorie(categorie)
                .tarifJournalier(new BigDecimal("150.00"))
                .build();
    }

    /**
     * Crée un client de test.
     */
    private Client creerClient(String nom, String email, String telephone) {
        return Client.builder()
                .nom(nom)
                .prenom("Test")
                .email(email)
                .telephone(telephone)
                .build();
    }
}
