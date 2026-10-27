package tn.esprit.autoloc;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.service.IEquipementService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour EquipementService (TP 4 - Couche Service).
 */
@SpringBootTest
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EquipementServiceTest {

    @Autowired
    private IEquipementService equipementService;

    private static Long idEquipementSauvegarde;

    @Test
    @Order(1)
    public void testCreateEquipement() {
        log.info("========== TEST 1: Créer un équipement ==========");

        Equipement equipement = new Equipement();
        equipement.setLibelle("GPS");
        equipement.setQuantite(10);

        Equipement equipementSauvegarde = equipementService.create(equipement);

        assertNotNull(equipementSauvegarde);
        assertNotNull(equipementSauvegarde.getIdEquipement());
        assertEquals("GPS", equipementSauvegarde.getLibelle());

        log.info("✓ Equipement créé avec succès, ID = {}", equipementSauvegarde.getIdEquipement());
        idEquipementSauvegarde = equipementSauvegarde.getIdEquipement();
    }

    @Test
    @Order(2)
    public void testFindAllEquipements() {
        log.info("========== TEST 2: Lister tous les équipements ==========");

        List<Equipement> equipements = equipementService.findAll();

        assertNotNull(equipements);
        assertTrue(equipements.size() > 0);

        log.info("✓ Nombre total d'équipements : {}", equipements.size());
    }

    @Test
    @Order(3)
    public void testFindById() {
        log.info("========== TEST 3: Rechercher un équipement par ID ==========");

        Equipement equipement = equipementService.findById(idEquipementSauvegarde);

        assertNotNull(equipement);
        assertEquals("GPS", equipement.getLibelle());

        log.info("✓ Equipement trouvé : {}", equipement.getLibelle());
    }

    @Test
    @Order(4)
    public void testUpdateEquipement() {
        log.info("========== TEST 4: Mettre à jour un équipement ==========");

        Equipement equipement = equipementService.findById(idEquipementSauvegarde);
        equipement.setLibelle("GPS Premium");
        equipement.setQuantite(15);

        Equipement equipementModifie = equipementService.update(idEquipementSauvegarde, equipement);

        assertEquals("GPS Premium", equipementModifie.getLibelle());

        log.info("✓ Equipement mis à jour : {}", equipementModifie.getLibelle());
    }

    @Test
    @Order(5)
    public void testDeleteEquipement() {
        log.info("========== TEST 5: Supprimer un équipement ==========");

        equipementService.deleteById(idEquipementSauvegarde);

        assertThrows(ResourceNotFoundException.class, () -> {
            equipementService.findById(idEquipementSauvegarde);
        });

        log.info("✓ Equipement supprimé avec ID : {}", idEquipementSauvegarde);
    }

    @Test
    @Order(6)
    public void testCreateEquipementSansLibelle() {
        log.info("========== TEST 6: Refuser création sans libellé ==========");

        Equipement equipement = new Equipement();
        equipement.setQuantite(5);

        assertThrows(IllegalArgumentException.class, () -> {
            equipementService.create(equipement);
        });

        log.info("✓ Règle métier respectée : libellé obligatoire");
    }

    @Test
    @Order(7)
    public void testCreateEquipementAvecQuantiteNegative() {
        log.info("========== TEST 7: Refuser création avec quantité négative ==========");

        Equipement equipement = new Equipement();
        equipement.setLibelle("Siège bébé");
        equipement.setQuantite(-5);

        assertThrows(IllegalArgumentException.class, () -> {
            equipementService.create(equipement);
        });

        log.info("✓ Règle métier respectée : quantité négative refusée");
    }
}
