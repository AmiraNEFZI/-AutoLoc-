package tn.esprit.autoloc;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.repository.IContratRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour ContratRepository (TP 3 - Couche Repository).
 */
@SpringBootTest
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ContratRepositoryTest {

    @Autowired
    private IContratRepository contratRepository;

    private static Long idContratSauvegarde;

    @Test
    @Order(1)
    public void testSaveContrat() {
        log.info("========== TEST 1: Save Contrat ==========");

        Contrat contrat = new Contrat();
        contrat.setDateSignature(LocalDate.now());
        contrat.setMontantTotal(new BigDecimal("560.00"));
        contrat.setValide(true);

        Contrat savedContrat = contratRepository.save(contrat);

        assertNotNull(savedContrat);
        assertNotNull(savedContrat.getIdContrat());
        assertEquals(new BigDecimal("560.00"), savedContrat.getMontantTotal());
        assertTrue(savedContrat.getValide());

        log.info("✓ Contrat sauvegardé avec ID: {}", savedContrat.getIdContrat());
        idContratSauvegarde = savedContrat.getIdContrat();
    }

    @Test
    @Order(2)
    public void testFindAll() {
        log.info("========== TEST 2: FindAll Contrats ==========");

        List<Contrat> contrats = (List<Contrat>) contratRepository.findAll();

        assertNotNull(contrats);
        assertTrue(contrats.size() > 0);

        log.info("✓ Nombre total de contrats: {}", contrats.size());
    }

    @Test
    @Order(3)
    public void testFindById() {
        log.info("========== TEST 3: FindById Contrat ==========");

        Optional<Contrat> optionalContrat = contratRepository.findById(idContratSauvegarde);

        assertTrue(optionalContrat.isPresent());
        assertEquals(new BigDecimal("560.00"), optionalContrat.get().getMontantTotal());

        log.info("✓ Contrat trouvé avec montant: {}", optionalContrat.get().getMontantTotal());
    }

    @Test
    @Order(4)
    public void testUpdateContrat() {
        log.info("========== TEST 4: Update Contrat ==========");

        Contrat contrat = contratRepository.findById(idContratSauvegarde).orElseThrow();
        contrat.setMontantTotal(new BigDecimal("650.00"));
        contrat.setValide(false);

        Contrat updatedContrat = contratRepository.save(contrat);

        assertEquals(new BigDecimal("650.00"), updatedContrat.getMontantTotal());
        assertFalse(updatedContrat.getValide());

        log.info("✓ Contrat mis à jour avec nouveau montant: {}", updatedContrat.getMontantTotal());
    }

    @Test
    @Order(5)
    public void testDeleteContrat() {
        log.info("========== TEST 5: Delete Contrat ==========");

        contratRepository.deleteById(idContratSauvegarde);

        Optional<Contrat> deletedContrat = contratRepository.findById(idContratSauvegarde);
        assertFalse(deletedContrat.isPresent());

        log.info("✓ Contrat supprimé avec ID: {}", idContratSauvegarde);
    }

    @Test
    @Order(6)
    public void testCount() {
        log.info("========== TEST 6: Count Contrats ==========");

        long count = contratRepository.count();

        assertTrue(count >= 0);
        log.info("✓ Nombre de contrats: {}", count);
    }
}
