package tn.esprit.autoloc;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.domain.enums.RoleEmploye;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.service.IEmployeService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour EmployeService (TP 4 - Couche Service).
 */
@SpringBootTest
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EmployeServiceTest {

    @Autowired
    private IEmployeService employeService;

    private static Long idEmployeSauvegarde;

    @Test
    @Order(1)
    public void testCreateEmploye() {
        log.info("========== TEST 1: Créer un employé ==========");

        Employe employe = new Employe();
        employe.setNom("Trabelsi");
        employe.setPrenom("Ahmed");
        employe.setRole(RoleEmploye.MANAGER);

        Employe employeSauvegarde = employeService.create(employe);

        assertNotNull(employeSauvegarde);
        assertNotNull(employeSauvegarde.getIdEmploye());
        assertEquals("Trabelsi", employeSauvegarde.getNom());
        assertEquals(RoleEmploye.MANAGER, employeSauvegarde.getRole());

        log.info("✓ Employé créé avec succès, ID = {}", employeSauvegarde.getIdEmploye());
        idEmployeSauvegarde = employeSauvegarde.getIdEmploye();
    }

    @Test
    @Order(2)
    public void testFindAllEmployes() {
        log.info("========== TEST 2: Lister tous les employés ==========");

        List<Employe> employes = employeService.findAll();

        assertNotNull(employes);
        assertTrue(employes.size() > 0);

        log.info("✓ Nombre total d'employés : {}", employes.size());
    }

    @Test
    @Order(3)
    public void testFindById() {
        log.info("========== TEST 3: Rechercher un employé par ID ==========");

        Employe employe = employeService.findById(idEmployeSauvegarde);

        assertNotNull(employe);
        assertEquals(idEmployeSauvegarde, employe.getIdEmploye());
        assertEquals("Trabelsi", employe.getNom());

        log.info("✓ Employé trouvé : {} {}", employe.getPrenom(), employe.getNom());
    }

    @Test
    @Order(4)
    public void testUpdateEmploye() {
        log.info("========== TEST 4: Mettre à jour un employé ==========");

        Employe employe = employeService.findById(idEmployeSauvegarde);
        employe.setRole(RoleEmploye.AGENT);

        Employe employeModifie = employeService.update(idEmployeSauvegarde, employe);

        assertEquals(RoleEmploye.AGENT, employeModifie.getRole());

        log.info("✓ Employé mis à jour avec nouveau rôle : {}", employeModifie.getRole());
    }

    @Test
    @Order(5)
    public void testDeleteEmploye() {
        log.info("========== TEST 5: Supprimer un employé ==========");

        employeService.deleteById(idEmployeSauvegarde);

        assertThrows(ResourceNotFoundException.class, () -> {
            employeService.findById(idEmployeSauvegarde);
        });

        log.info("✓ Employé supprimé avec ID : {}", idEmployeSauvegarde);
    }

    @Test
    @Order(6)
    public void testCreateEmployeAvecIdNonNull() {
        log.info("========== TEST 6: Refuser création avec ID non null ==========");

        Employe employe = new Employe();
        employe.setIdEmploye(999L);
        employe.setNom("Test");
        employe.setRole(RoleEmploye.AGENT);

        assertThrows(IllegalArgumentException.class, () -> {
            employeService.create(employe);
        });

        log.info("✓ Règle métier respectée : création refusée avec ID non null");
    }

    @Test
    @Order(7)
    public void testCreateEmployeSansNom() {
        log.info("========== TEST 7: Refuser création sans nom ==========");

        Employe employe = new Employe();
        employe.setRole(RoleEmploye.AGENT);

        assertThrows(IllegalArgumentException.class, () -> {
            employeService.create(employe);
        });

        log.info("✓ Règle métier respectée : nom obligatoire");
    }

    @Test
    @Order(8)
    public void testCreateEmployeSansRole() {
        log.info("========== TEST 8: Refuser création sans rôle ==========");

        Employe employe = new Employe();
        employe.setNom("Test");

        assertThrows(IllegalArgumentException.class, () -> {
            employeService.create(employe);
        });

        log.info("✓ Règle métier respectée : rôle obligatoire");
    }
}
