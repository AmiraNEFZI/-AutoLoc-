package tn.esprit.autoloc;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.domain.enums.CategorieVehicule;
import tn.esprit.autoloc.domain.enums.StatutVehicule;
import tn.esprit.autoloc.repository.IVehiculeRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour VehiculeRepository (TP 3 - Couche Repository).
 */
@SpringBootTest
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class VehiculeRepositoryTest {

    @Autowired
    private IVehiculeRepository vehiculeRepository;

    private static Long idVehiculeSauvegarde;

    @Test
    @Order(1)
    public void testSaveVehicule() {
        log.info("========== TEST 1: Save Vehicule ==========");

        Vehicule vehicule = new Vehicule();
        vehicule.setImmatriculation("123TU456");
        vehicule.setMarque("Renault");
        vehicule.setModele("Clio");
        vehicule.setCategorie(CategorieVehicule.CITADINE);
        vehicule.setTarifJournalier(new BigDecimal("80.00"));
        vehicule.setStatut(StatutVehicule.DISPONIBLE);

        Vehicule savedVehicule = vehiculeRepository.save(vehicule);

        assertNotNull(savedVehicule);
        assertNotNull(savedVehicule.getIdVehicule());
        assertEquals("123TU456", savedVehicule.getImmatriculation());
        assertEquals(StatutVehicule.DISPONIBLE, savedVehicule.getStatut());

        log.info("✓ Véhicule sauvegardé avec ID: {}", savedVehicule.getIdVehicule());
        idVehiculeSauvegarde = savedVehicule.getIdVehicule();
    }

    @Test
    @Order(2)
    public void testFindAll() {
        log.info("========== TEST 2: FindAll Vehicules ==========");

        List<Vehicule> vehicules = (List<Vehicule>) vehiculeRepository.findAll();

        assertNotNull(vehicules);
        assertTrue(vehicules.size() > 0);

        log.info("✓ Nombre total de véhicules: {}", vehicules.size());
    }

    @Test
    @Order(3)
    public void testFindById() {
        log.info("========== TEST 3: FindById Vehicule ==========");

        Optional<Vehicule> optionalVehicule = vehiculeRepository.findById(idVehiculeSauvegarde);

        assertTrue(optionalVehicule.isPresent());
        assertEquals("123TU456", optionalVehicule.get().getImmatriculation());

        log.info("✓ Véhicule trouvé: {} {}", optionalVehicule.get().getMarque(), optionalVehicule.get().getModele());
    }

    @Test
    @Order(4)
    public void testUpdateVehicule() {
        log.info("========== TEST 4: Update Vehicule ==========");

        Vehicule vehicule = vehiculeRepository.findById(idVehiculeSauvegarde).orElseThrow();
        vehicule.setStatut(StatutVehicule.LOUE);
        vehicule.setTarifJournalier(new BigDecimal("90.00"));

        Vehicule updatedVehicule = vehiculeRepository.save(vehicule);

        assertEquals(StatutVehicule.LOUE, updatedVehicule.getStatut());
        assertEquals(new BigDecimal("90.00"), updatedVehicule.getTarifJournalier());

        log.info("✓ Véhicule mis à jour: statut={}", updatedVehicule.getStatut());
    }

    @Test
    @Order(5)
    public void testDeleteVehicule() {
        log.info("========== TEST 5: Delete Vehicule ==========");

        vehiculeRepository.deleteById(idVehiculeSauvegarde);

        Optional<Vehicule> deletedVehicule = vehiculeRepository.findById(idVehiculeSauvegarde);
        assertFalse(deletedVehicule.isPresent());

        log.info("✓ Véhicule supprimé avec ID: {}", idVehiculeSauvegarde);
    }

    @Test
    @Order(6)
    public void testCount() {
        log.info("========== TEST 6: Count Vehicules ==========");

        long count = vehiculeRepository.count();

        assertTrue(count >= 0);
        log.info("✓ Nombre de véhicules: {}", count);
    }
}
