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
import tn.esprit.autoloc.repository.IPaiementRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour PaiementRepository (TP 3 - Couche Repository).
 */
@SpringBootTest
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PaiementRepositoryTest {

    @Autowired
    private IPaiementRepository paiementRepository;

    private static Long idPaiementSauvegarde;

    @Test
    @Order(1)
    public void testSavePaiement() {
        log.info("========== TEST 1: Save Paiement ==========");

        Paiement paiement = new Paiement();
        paiement.setMontant(new BigDecimal("200.00"));
        paiement.setDatePaiement(LocalDate.now());
        paiement.setModePaiement(ModePaiement.CARTE);

        Paiement savedPaiement = paiementRepository.save(paiement);

        assertNotNull(savedPaiement);
        assertNotNull(savedPaiement.getIdPaiement());
        assertEquals(new BigDecimal("200.00"), savedPaiement.getMontant());
        assertEquals(ModePaiement.CARTE, savedPaiement.getModePaiement());

        log.info("✓ Paiement sauvegardé avec ID: {}", savedPaiement.getIdPaiement());
        idPaiementSauvegarde = savedPaiement.getIdPaiement();
    }

    @Test
    @Order(2)
    public void testFindAll() {
        log.info("========== TEST 2: FindAll Paiements ==========");

        List<Paiement> paiements = (List<Paiement>) paiementRepository.findAll();

        assertNotNull(paiements);
        assertTrue(paiements.size() > 0);

        log.info("✓ Nombre total de paiements: {}", paiements.size());
    }

    @Test
    @Order(3)
    public void testFindById() {
        log.info("========== TEST 3: FindById Paiement ==========");

        Optional<Paiement> optionalPaiement = paiementRepository.findById(idPaiementSauvegarde);

        assertTrue(optionalPaiement.isPresent());
        assertEquals(new BigDecimal("200.00"), optionalPaiement.get().getMontant());

        log.info("✓ Paiement trouvé avec montant: {}", optionalPaiement.get().getMontant());
    }

    @Test
    @Order(4)
    public void testUpdatePaiement() {
        log.info("========== TEST 4: Update Paiement ==========");

        Paiement paiement = paiementRepository.findById(idPaiementSauvegarde).orElseThrow();
        paiement.setMontant(new BigDecimal("250.00"));
        paiement.setModePaiement(ModePaiement.ESPECES);

        Paiement updatedPaiement = paiementRepository.save(paiement);

        assertEquals(new BigDecimal("250.00"), updatedPaiement.getMontant());
        assertEquals(ModePaiement.ESPECES, updatedPaiement.getModePaiement());

        log.info("✓ Paiement mis à jour avec nouveau montant: {}", updatedPaiement.getMontant());
    }

    @Test
    @Order(5)
    public void testDeletePaiement() {
        log.info("========== TEST 5: Delete Paiement ==========");

        paiementRepository.deleteById(idPaiementSauvegarde);

        Optional<Paiement> deletedPaiement = paiementRepository.findById(idPaiementSauvegarde);
        assertFalse(deletedPaiement.isPresent());

        log.info("✓ Paiement supprimé avec ID: {}", idPaiementSauvegarde);
    }

    @Test
    @Order(6)
    public void testCount() {
        log.info("========== TEST 6: Count Paiements ==========");

        long count = paiementRepository.count();

        assertTrue(count >= 0);
        log.info("✓ Nombre de paiements: {}", count);
    }
}
