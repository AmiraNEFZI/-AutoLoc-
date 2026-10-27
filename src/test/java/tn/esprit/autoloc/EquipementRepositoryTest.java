package tn.esprit.autoloc;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.repository.IEquipementRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour EquipementRepository (TP 3 - Couche Repository).
 */
@SpringBootTest
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EquipementRepositoryTest {

    @Autowired
    private IEquipementRepository equipementRepository;

    private static Long idEquipementSauvegarde;

    @Test
    @Order(1)
    public void testSaveEquipement() {
        log.info("========== TEST 1: Save Equipement ==========");

        Equipement equipement = new Equipement();
        equipement.setLibelle("GPS");
        equipement.setQuantite(10);

        Equipement savedEquipement = equipementRepository.save(equipement);

        assertNotNull(savedEquipement);
        assertNotNull(savedEquipement.getIdEquipement());
        assertEquals("GPS", savedEquipement.getLibelle());
        assertEquals(10, savedEquipement.getQuantite());

        log.info("✓ Equipement sauvegardé avec ID: {}", savedEquipement.getIdEquipement());
        idEquipementSauvegarde = savedEquipement.getIdEquipement();
    }

    @Test
    @Order(2)
    public void testFindAll() {
        log.info("========== TEST 2: FindAll Equipements ==========");

        List<Equipement> equipements = (List<Equipement>) equipementRepository.findAll();

        assertNotNull(equipements);
        assertTrue(equipements.size() > 0);

        log.info("✓ Nombre total d'équipements: {}", equipements.size());
    }

    @Test
    @Order(3)
    public void testFindById() {
        log.info("========== TEST 3: FindById Equipement ==========");

        Optional<Equipement> optionalEquipement = equipementRepository.findById(idEquipementSauvegarde);

        assertTrue(optionalEquipement.isPresent());
        assertEquals("GPS", optionalEquipement.get().getLibelle());

        log.info("✓ Equipement trouvé: {}", optionalEquipement.get().getLibelle());
    }

    @Test
    @Order(4)
    public void testUpdateEquipement() {
        log.info("========== TEST 4: Update Equipement ==========");

        Equipement equipement = equipementRepository.findById(idEquipementSauvegarde).orElseThrow();
        equipement.setQuantite(15);
        equipement.setLibelle("GPS Premium");

        Equipement updatedEquipement = equipementRepository.save(equipement);

        assertEquals("GPS Premium", updatedEquipement.getLibelle());
        assertEquals(15, updatedEquipement.getQuantite());

        log.info("✓ Equipement mis à jour: {}", updatedEquipement.getLibelle());
    }

    @Test
    @Order(5)
    public void testDeleteEquipement() {
        log.info("========== TEST 5: Delete Equipement ==========");

        equipementRepository.deleteById(idEquipementSauvegarde);

        Optional<Equipement> deletedEquipement = equipementRepository.findById(idEquipementSauvegarde);
        assertFalse(deletedEquipement.isPresent());

        log.info("✓ Equipement supprimé avec ID: {}", idEquipementSauvegarde);
    }

    @Test
    @Order(6)
    public void testCount() {
        log.info("========== TEST 6: Count Equipements ==========");

        long count = equipementRepository.count();

        assertTrue(count >= 0);
        log.info("✓ Nombre d'équipements: {}", count);
    }
}
