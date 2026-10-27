package tn.esprit.autoloc;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.domain.enums.ModePaiement;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IPaiementRepository;
import tn.esprit.autoloc.service.IPaiementService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour PaiementService (TP 4 - Couche Service).
 * Note: PaiementService est en LECTURE SEULE (pas de create/update/delete).
 * Les paiements sont créés automatiquement via les contrats.
 */
@SpringBootTest
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PaiementServiceTest {

    @Autowired
    private IPaiementService paiementService;

    @Autowired
    private IPaiementRepository paiementRepository; // Pour créer des données de test

    private static Long idPaiementTest;

    @Test
    @Order(1)
    public void testPreparerDonneesTest() {
        log.info("========== TEST 1: Préparer un paiement pour les tests ==========");

        // Créer un paiement directement via le repository (car service est en lecture seule)
        Paiement paiement = new Paiement();
        paiement.setMontant(new BigDecimal("200.00"));
        paiement.setDatePaiement(LocalDate.now());
        paiement.setModePaiement(ModePaiement.CARTE);

        Paiement paiementSauvegarde = paiementRepository.save(paiement);

        assertNotNull(paiementSauvegarde);
        assertNotNull(paiementSauvegarde.getIdPaiement());

        log.info("✓ Paiement de test créé avec ID: {}", paiementSauvegarde.getIdPaiement());
        idPaiementTest = paiementSauvegarde.getIdPaiement();
    }

    @Test
    @Order(2)
    public void testFindById() {
        log.info("========== TEST 2: Rechercher un paiement par ID (Service) ==========");

        Paiement paiement = paiementService.findById(idPaiementTest);

        assertNotNull(paiement);
        assertEquals(idPaiementTest, paiement.getIdPaiement());
        assertEquals(new BigDecimal("200.00"), paiement.getMontant());

        log.info("✓ Paiement trouvé via Service : {} DT", paiement.getMontant());
    }

    @Test
    @Order(3)
    public void testFindAll() {
        log.info("========== TEST 3: Lister tous les paiements (Service) ==========");

        List<Paiement> paiements = paiementService.findAll();

        assertNotNull(paiements);
        assertTrue(paiements.size() > 0);

        log.info("✓ Nombre total de paiements : {}", paiements.size());
        paiements.forEach(p -> log.info("  - Paiement ID={}, Montant={}, Mode={}", 
                                        p.getIdPaiement(), p.getMontant(), p.getModePaiement()));
    }

    @Test
    @Order(4)
    public void testFindByIdPaiementInexistant() {
        log.info("========== TEST 4: Rechercher un paiement inexistant ==========");

        assertThrows(ResourceNotFoundException.class, () -> {
            paiementService.findById(99999L);
        });

        log.info("✓ ResourceNotFoundException levée pour paiement inexistant");
    }

    @Test
    @Order(5)
    public void testNettoyerDonneesTest() {
        log.info("========== TEST 5: Nettoyer les données de test ==========");

        // Nettoyer le paiement créé pour les tests
        paiementRepository.deleteById(idPaiementTest);

        log.info("✓ Paiement de test supprimé avec ID: {}", idPaiementTest);
    }
}
