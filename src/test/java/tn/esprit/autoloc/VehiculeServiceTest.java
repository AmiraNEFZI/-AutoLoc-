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
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.service.IVehiculeService;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour VehiculeService (TP 4 - Couche Service).
 */
@SpringBootTest
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class VehiculeServiceTest {

    @Autowired
    private IVehiculeService vehiculeService;

    private static Long idVehiculeSauvegarde;

    @Test
    @Order(1)
    public void testCreateVehicule() {
        log.info("========== TEST 1: Créer un véhicule ==========");

        Vehicule vehicule = new Vehicule();
        vehicule.setImmatriculation("123TU456");
        vehicule.setMarque("Renault");
        vehicule.setModele("Clio");
        vehicule.setCategorie(CategorieVehicule.CITADINE);
        vehicule.setTarifJournalier(new BigDecimal("80.00"));
        vehicule.setStatut(StatutVehicule.DISPONIBLE);

        Vehicule vehiculeSauvegarde = vehiculeService.create(vehicule);

        assertNotNull(vehiculeSauvegarde);
        assertNotNull(vehiculeSauvegarde.getIdVehicule());
        assertEquals("123TU456", vehiculeSauvegarde.getImmatriculation());

        log.info("✓ Véhicule créé avec succès, ID = {}", vehiculeSauvegarde.getIdVehicule());
        idVehiculeSauvegarde = vehiculeSauvegarde.getIdVehicule();
    }

    @Test
    @Order(2)
    public void testFindAllVehicules() {
        log.info("========== TEST 2: Lister tous les véhicules ==========");

        List<Vehicule> vehicules = vehiculeService.findAll();

        assertNotNull(vehicules);
        assertTrue(vehicules.size() > 0);

        log.info("✓ Nombre total de véhicules : {}", vehicules.size());
    }

    @Test
    @Order(3)
    public void testFindById() {
        log.info("========== TEST 3: Rechercher un véhicule par ID ==========");

        Vehicule vehicule = vehiculeService.findById(idVehiculeSauvegarde);

        assertNotNull(vehicule);
        assertEquals("123TU456", vehicule.getImmatriculation());

        log.info("✓ Véhicule trouvé : {} {}", vehicule.getMarque(), vehicule.getModele());
    }

    @Test
    @Order(4)
    public void testUpdateVehicule() {
        log.info("========== TEST 4: Mettre à jour un véhicule ==========");

        Vehicule vehicule = vehiculeService.findById(idVehiculeSauvegarde);
        vehicule.setStatut(StatutVehicule.LOUE);
        vehicule.setTarifJournalier(new BigDecimal("90.00"));

        Vehicule vehiculeModifie = vehiculeService.update(idVehiculeSauvegarde, vehicule);

        assertEquals(StatutVehicule.LOUE, vehiculeModifie.getStatut());

        log.info("✓ Véhicule mis à jour : statut={}", vehiculeModifie.getStatut());
    }

    @Test
    @Order(5)
    public void testDeleteVehicule() {
        log.info("========== TEST 5: Supprimer un véhicule ==========");

        vehiculeService.deleteById(idVehiculeSauvegarde);

        assertThrows(ResourceNotFoundException.class, () -> {
            vehiculeService.findById(idVehiculeSauvegarde);
        });

        log.info("✓ Véhicule supprimé avec ID : {}", idVehiculeSauvegarde);
    }

    @Test
    @Order(6)
    public void testCreateVehiculeAvecTarifNegatif() {
        log.info("========== TEST 6: Refuser création avec tarif négatif ==========");

        Vehicule vehicule = new Vehicule();
        vehicule.setImmatriculation("789TU012");
        vehicule.setMarque("Peugeot");
        vehicule.setModele("208");
        vehicule.setCategorie(CategorieVehicule.CITADINE);
        vehicule.setTarifJournalier(new BigDecimal("-50.00"));
        vehicule.setStatut(StatutVehicule.DISPONIBLE);

        assertThrows(IllegalArgumentException.class, () -> {
            vehiculeService.create(vehicule);
        });

        log.info("✓ Règle métier respectée : tarif négatif refusé");
    }
}
