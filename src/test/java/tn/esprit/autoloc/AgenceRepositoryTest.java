package tn.esprit.autoloc;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.repository.IAgenceRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour AgenceRepository (TP 3 - Couche Repository).
 */
@SpringBootTest
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AgenceRepositoryTest {

    @Autowired
    private IAgenceRepository agenceRepository;

    private static Long idAgenceSauvegardee;

    @Test
    @Order(1)
    public void testSaveAgence() {
        log.info("========== TEST 1: Save Agence ==========");

        Agence agence = new Agence();
        agence.setNom("Agence Tunis Centre");
        agence.setVille("Tunis");
        agence.setAdresse("Avenue Habib Bourguiba");
        agence.setTelephone("+216 71 123 456");

        Agence savedAgence = agenceRepository.save(agence);

        assertNotNull(savedAgence);
        assertNotNull(savedAgence.getIdAgence());
        assertEquals("Agence Tunis Centre", savedAgence.getNom());

        log.info("✓ Agence sauvegardée avec ID: {}", savedAgence.getIdAgence());
        idAgenceSauvegardee = savedAgence.getIdAgence();
    }

    @Test
    @Order(2)
    public void testFindAll() {
        log.info("========== TEST 2: FindAll Agences ==========");

        List<Agence> agences = (List<Agence>) agenceRepository.findAll();

        assertNotNull(agences);
        assertTrue(agences.size() > 0);

        log.info("✓ Nombre total d'agences: {}", agences.size());
    }

    @Test
    @Order(3)
    public void testFindById() {
        log.info("========== TEST 3: FindById Agence ==========");

        Optional<Agence> optionalAgence = agenceRepository.findById(idAgenceSauvegardee);

        assertTrue(optionalAgence.isPresent());
        assertEquals("Agence Tunis Centre", optionalAgence.get().getNom());

        log.info("✓ Agence trouvée: {}", optionalAgence.get().getNom());
    }

    @Test
    @Order(4)
    public void testUpdateAgence() {
        log.info("========== TEST 4: Update Agence ==========");

        Agence agence = agenceRepository.findById(idAgenceSauvegardee).orElseThrow();
        agence.setNom("Agence Tunis Modernisée");
        agence.setVille("Tunis Capitale");

        Agence updatedAgence = agenceRepository.save(agence);

        assertEquals("Agence Tunis Modernisée", updatedAgence.getNom());
        assertEquals("Tunis Capitale", updatedAgence.getVille());

        log.info("✓ Agence mise à jour: {}", updatedAgence.getNom());
    }

    @Test
    @Order(5)
    public void testDeleteAgence() {
        log.info("========== TEST 5: Delete Agence ==========");

        agenceRepository.deleteById(idAgenceSauvegardee);

        Optional<Agence> deletedAgence = agenceRepository.findById(idAgenceSauvegardee);
        assertFalse(deletedAgence.isPresent());

        log.info("✓ Agence supprimée avec ID: {}", idAgenceSauvegardee);
    }

    @Test
    @Order(6)
    public void testCount() {
        log.info("========== TEST 6: Count Agences ==========");

        long count = agenceRepository.count();

        assertTrue(count >= 0);
        log.info("✓ Nombre d'agences: {}", count);
    }
}
