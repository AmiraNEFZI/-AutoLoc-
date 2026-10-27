package tn.esprit.autoloc;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.autoloc.domain.Maintenance;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.service.IMaintenanceService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour MaintenanceService (TP 4 - Couche Service).
 */
@SpringBootTest
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MaintenanceServiceTest {

    @Autowired
    private IMaintenanceService maintenanceService;

    private static Long idMaintenanceSauvegardee;

    @Test
    @Order(1)
    public void testCreateMaintenance() {
        log.info("========== TEST 1: Créer une maintenance ==========");

        Maintenance maintenance = new Maintenance();
        maintenance.setDateDebut(LocalDate.now());
        maintenance.setDateFin(LocalDate.now().plusDays(3));
        maintenance.setDescription("Révision complète");

        Maintenance maintenanceSauvegardee = maintenanceService.create(maintenance);

        assertNotNull(maintenanceSauvegardee);
        assertNotNull(maintenanceSauvegardee.getIdMaintenance());
        assertEquals("Révision complète", maintenanceSauvegardee.getDescription());

        log.info("✓ Maintenance créée avec succès, ID = {}", maintenanceSauvegardee.getIdMaintenance());
        idMaintenanceSauvegardee = maintenanceSauvegardee.getIdMaintenance();
    }

    @Test
    @Order(2)
    public void testFindAllMaintenances() {
        log.info("========== TEST 2: Lister toutes les maintenances ==========");

        List<Maintenance> maintenances = maintenanceService.findAll();

        assertNotNull(maintenances);
        assertTrue(maintenances.size() > 0);

        log.info("✓ Nombre total de maintenances : {}", maintenances.size());
    }

    @Test
    @Order(3)
    public void testFindById() {
        log.info("========== TEST 3: Rechercher une maintenance par ID ==========");

        Maintenance maintenance = maintenanceService.findById(idMaintenanceSauvegardee);

        assertNotNull(maintenance);
        assertEquals("Révision complète", maintenance.getDescription());

        log.info("✓ Maintenance trouvée : {}", maintenance.getDescription());
    }

    @Test
    @Order(4)
    public void testUpdateMaintenance() {
        log.info("========== TEST 4: Mettre à jour une maintenance ==========");

        Maintenance maintenance = maintenanceService.findById(idMaintenanceSauvegardee);
        maintenance.setDescription("Révision complète + Vidange");
        maintenance.setDateFin(LocalDate.now().plusDays(5));

        Maintenance maintenanceModifiee = maintenanceService.update(idMaintenanceSauvegardee, maintenance);

        assertEquals("Révision complète + Vidange", maintenanceModifiee.getDescription());

        log.info("✓ Maintenance mise à jour : {}", maintenanceModifiee.getDescription());
    }

    @Test
    @Order(5)
    public void testDeleteMaintenance() {
        log.info("========== TEST 5: Supprimer une maintenance ==========");

        maintenanceService.deleteById(idMaintenanceSauvegardee);

        assertThrows(ResourceNotFoundException.class, () -> {
            maintenanceService.findById(idMaintenanceSauvegardee);
        });

        log.info("✓ Maintenance supprimée avec ID : {}", idMaintenanceSauvegardee);
    }

    @Test
    @Order(6)
    public void testCreateMaintenanceAvecDateFinAvantDebut() {
        log.info("========== TEST 6: Refuser création avec dateFin avant dateDebut ==========");

        Maintenance maintenance = new Maintenance();
        maintenance.setDateDebut(LocalDate.now());
        maintenance.setDateFin(LocalDate.now().minusDays(2)); // Date de fin AVANT date de début
        maintenance.setDescription("Test");

        assertThrows(IllegalArgumentException.class, () -> {
            maintenanceService.create(maintenance);
        });

        log.info("✓ Règle métier respectée : dateFin ne peut pas être avant dateDebut");
    }
}
