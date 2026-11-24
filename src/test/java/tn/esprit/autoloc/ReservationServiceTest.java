package tn.esprit.autoloc;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Tests unitaires pour ReservationService (TP 4 - Couche Service).
 * 
 * NOTE: Ces tests sont désactivés car l'interface IReservationService
 * a été modifiée dans le TP 7 pour utiliser des DTO au lieu d'entités JPA directement.
 * 
 * Les nouveaux tests utilisent :
 * - ReservationRequestDTO (entrée)
 * - ReservationResponseDTO (sortie)
 * 
 * Pour les tests actuels, voir : DesignPatternsTest.java et AffectationServiceTest.java
 */
@SpringBootTest
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Disabled("Tests obsolètes - L'interface a été modifiée pour utiliser des DTO (TP 7)")
public class ReservationServiceTest {

    @Test
    void testsObsoletes() {
        log.info("Ces tests sont obsolètes depuis le TP 7 (DTO)");
        log.info("Utilisez DesignPatternsTest.java pour les tests actuels");
    }
}
