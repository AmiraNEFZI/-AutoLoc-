package tn.esprit.autoloc;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.autoloc.domain.Maintenance;
import tn.esprit.autoloc.repository.IMaintenanceRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour MaintenanceRepository (TP 3 - Couche Repository).
 */
@SpringBootTest
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MaintenanceRepositoryTest {

    @Autowired
    private IMaintenanceRepository maintenanceRepository;

    private static Long idMaintenanceSauvegardee;

    @Test
    @Order(1)
    public void testSaveMaintenance() {
        log.info("========== TEST 1: Save Maintenance ==========");

        Maintenance maintenance = new Maintenance();
        maintenance.setDateDebut(LocalDate.now());
        maintenance.setDateFin(LocalDate.now().plusDays(3));
        maintenance.setDescription("Révision complète");

        Maintenance savedMaintenance = maintenanceRepository.save(maintenance);

        assertNotNull(savedMaintenance);
        assertNotNull(savedMaintenance.getIdMaintenance());
        assertEquals("Révision complète", savedMaintenance.getDescription());

        log.info("✓ Maintenance sauvegardée avec ID: {}", savedMaintenance.getIdMaintenance());
        idMaintenanceSauvegardee = savedMaintenance.getIdMaintenance();
    }

    @Test
    @Order(2)
    public void testFindAll() {
        log.info("========== TEST 2: FindAll Maintenances ==========");

        List<Maintenance> maintenances = (List<Maintenance>) maintenanceRepository.findAll();

        assertNotNull(maintenances);
        assertTrue(maintenances.size() > 0);

        log.info("✓ Nombre total de maintenances: {}", maintenances.size());
    }

    @Test
    @Order(3)
    public void testFindById() {
        log.info("========== TEST 3: FindById Maintenance ==========");

        Optional<Maintenance> optionalMaintenance = maintenanceRepository.findById(idMaintenanceSauvegardee);

        assertTrue(optionalMaintenance.isPresent());
        assertEquals("Révision complète", optionalMaintenance.get().getDescription());

        log.info("✓ Maintenance trouvée: {}", optionalMaintenance.get().getDescription());
    }

    @Test
    @Order(4)
    public void testUpdateMaintenance() {
        log.info("========== TEST 4: Update Maintenance ==========");

        Maintenance maintenance = maintenanceRepository.findById(idMaintenanceSauvegardee).orElseThrow();
        maintenance.setDescription("Révision complète + Vidange");
        maintenance.setDateFin(LocalDate.now().plusDays(5));

        Maintenance updatedMaintenance = maintenanceRepository.save(maintenance);

        assertEquals("Révision complète + Vidange", updatedMaintenance.getDescription());

        log.info("✓ Maintenance mise à jour: {}", updatedMaintenance.getDescription());
    }

    @Test
    @Order(5)
    public void testDeleteMaintenance() {
        log.info("========== TEST 5: Delete Maintenance ==========");

        maintenanceRepository.deleteById(idMaintenanceSauvegardee);

        Optional<Maintenance> deletedMaintenance = maintenanceRepository.findById(idMaintenanceSauvegardee);
        assertFalse(deletedMaintenance.isPresent());

        log.info("✓ Maintenance supprimée avec ID: {}", idMaintenanceSauvegardee);
    }

    @Test
    @Order(6)
    public void testCount() {
        log.info("========== TEST 6: Count Maintenances ==========");

        long count = maintenanceRepository.count();

        assertTrue(count >= 0);
        log.info("✓ Nombre de maintenances: {}", count);
    }
}
