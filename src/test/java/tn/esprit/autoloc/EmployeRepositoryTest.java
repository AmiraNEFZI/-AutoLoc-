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
import tn.esprit.autoloc.repository.IEmployeRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour EmployeRepository (TP 3 - Couche Repository).
 */
@SpringBootTest
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EmployeRepositoryTest {

    @Autowired
    private IEmployeRepository employeRepository;

    private static Long idEmployeSauvegarde;

    @Test
    @Order(1)
    public void testSaveEmploye() {
        log.info("========== TEST 1: Save Employe ==========");

        Employe employe = new Employe();
        employe.setNom("Trabelsi");
        employe.setPrenom("Ahmed");
        employe.setRole(RoleEmploye.MANAGER);

        Employe savedEmploye = employeRepository.save(employe);

        assertNotNull(savedEmploye);
        assertNotNull(savedEmploye.getIdEmploye());
        assertEquals("Trabelsi", savedEmploye.getNom());
        assertEquals(RoleEmploye.MANAGER, savedEmploye.getRole());

        log.info("✓ Employé sauvegardé avec ID: {}", savedEmploye.getIdEmploye());
        idEmployeSauvegarde = savedEmploye.getIdEmploye();
    }

    @Test
    @Order(2)
    public void testFindAll() {
        log.info("========== TEST 2: FindAll Employes ==========");

        List<Employe> employes = (List<Employe>) employeRepository.findAll();

        assertNotNull(employes);
        assertTrue(employes.size() > 0);

        log.info("✓ Nombre total d'employés: {}", employes.size());
    }

    @Test
    @Order(3)
    public void testFindById() {
        log.info("========== TEST 3: FindById Employe ==========");

        Optional<Employe> optionalEmploye = employeRepository.findById(idEmployeSauvegarde);

        assertTrue(optionalEmploye.isPresent());
        assertEquals("Trabelsi", optionalEmploye.get().getNom());

        log.info("✓ Employé trouvé: {} {}", optionalEmploye.get().getPrenom(), optionalEmploye.get().getNom());
    }

    @Test
    @Order(4)
    public void testUpdateEmploye() {
        log.info("========== TEST 4: Update Employe ==========");

        Employe employe = employeRepository.findById(idEmployeSauvegarde).orElseThrow();
        employe.setRole(RoleEmploye.AGENT);

        Employe updatedEmploye = employeRepository.save(employe);

        assertEquals(RoleEmploye.AGENT, updatedEmploye.getRole());

        log.info("✓ Employé mis à jour avec nouveau rôle: {}", updatedEmploye.getRole());
    }

    @Test
    @Order(5)
    public void testDeleteEmploye() {
        log.info("========== TEST 5: Delete Employe ==========");

        employeRepository.deleteById(idEmployeSauvegarde);

        Optional<Employe> deletedEmploye = employeRepository.findById(idEmployeSauvegarde);
        assertFalse(deletedEmploye.isPresent());

        log.info("✓ Employé supprimé avec ID: {}", idEmployeSauvegarde);
    }

    @Test
    @Order(6)
    public void testCount() {
        log.info("========== TEST 6: Count Employes ==========");

        long count = employeRepository.count();

        assertTrue(count >= 0);
        log.info("✓ Nombre d'employés: {}", count);
    }
}
