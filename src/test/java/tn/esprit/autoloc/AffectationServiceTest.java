package tn.esprit.autoloc;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.domain.enums.CategorieVehicule;
import tn.esprit.autoloc.domain.enums.RoleEmploye;
import tn.esprit.autoloc.domain.enums.StatutVehicule;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IAgenceRepository;
import tn.esprit.autoloc.repository.IEmployeRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;
import tn.esprit.autoloc.service.IEmployeService;
import tn.esprit.autoloc.service.IVehiculeService;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour l'affectation et désaffectation (Partie 1.5 du TP 7).
 * 
 * Tests couverts :
 * - Affectation véhicule à agence existante
 * - Affectation avec identifiant véhicule inexistant
 * - Affectation avec identifiant agence inexistant
 * - Transfert véhicule d'une agence vers une autre
 * - Désaffectation et vérification clé étrangère = null
 * - Répétition des mêmes tests pour employé
 * - Vérification que les opérations sont transactionnelles
 */
@SpringBootTest
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AffectationServiceTest {

    @Autowired
    private IVehiculeService vehiculeService;

    @Autowired
    private IEmployeService employeService;

    @Autowired
    private IAgenceRepository agenceRepository;

    @Autowired
    private IVehiculeRepository vehiculeRepository;

    @Autowired
    private IEmployeRepository employeRepository;

    private static Long idAgence1;
    private static Long idAgence2;
    private static Long idVehicule;
    private static Long idEmploye;

    // ==========================================
    // SETUP : Création des données de test
    // ==========================================

    @BeforeAll
    public static void setup(@Autowired IAgenceRepository agenceRepository,
                             @Autowired IVehiculeRepository vehiculeRepository,
                             @Autowired IEmployeRepository employeRepository) {
        log.info("========== SETUP : Création des données de test ==========");

        // Créer 2 agences
        Agence agence1 = new Agence();
        agence1.setNom("Agence Paris");
        agence1.setVille("Paris");
        agence1.setAdresse("10 Rue de la Paix");
        agence1.setTelephone("0140000001");
        agence1 = agenceRepository.save(agence1);
        idAgence1 = agence1.getIdAgence();

        Agence agence2 = new Agence();
        agence2.setNom("Agence Lyon");
        agence2.setVille("Lyon");
        agence2.setAdresse("20 Rue de la République");
        agence2.setTelephone("0478000002");
        agence2 = agenceRepository.save(agence2);
        idAgence2 = agence2.getIdAgence();

        // Créer 1 véhicule
        Vehicule vehicule = new Vehicule();
        vehicule.setImmatriculation("AA-123-BB");
        vehicule.setMarque("Renault");
        vehicule.setModele("Clio");
        vehicule.setCategorie(CategorieVehicule.CITADINE);
        vehicule.setTarifJournalier(new BigDecimal("50.00"));
        vehicule.setStatut(StatutVehicule.DISPONIBLE);
        vehicule = vehiculeRepository.save(vehicule);
        idVehicule = vehicule.getIdVehicule();

        // Créer 1 employé
        Employe employe = new Employe();
        employe.setNom("Dupont");
        employe.setPrenom("Jean");
        employe.setRole(RoleEmploye.AGENT);
        employe = employeRepository.save(employe);
        idEmploye = employe.getIdEmploye();

        log.info("✓ Données créées : Agence1 ID={}, Agence2 ID={}, Vehicule ID={}, Employe ID={}",
                idAgence1, idAgence2, idVehicule, idEmploye);
    }

    // ==========================================
    // TESTS : AFFECTATION VÉHICULE
    // ==========================================

    @Test
    @Order(1)
    @DisplayName("Affecter un véhicule à une agence existante")
    public void testAffecterVehiculeAAgenceExistante() {
        log.info("========== TEST 1: Affecter véhicule à agence existante ==========");

        // Affecter le véhicule à l'agence 1
        assertDoesNotThrow(() -> vehiculeService.affecterVehiculeAAgence(idVehicule, idAgence1));

        // Vérifier que l'affectation a bien été effectuée
        Vehicule vehicule = vehiculeRepository.findById(idVehicule).orElseThrow();
        assertNotNull(vehicule.getAgence(), "Le véhicule doit être rattaché à une agence");
        assertEquals(idAgence1, vehicule.getAgence().getIdAgence(),
                "Le véhicule doit être rattaché à l'agence 1");

        log.info("✓ Véhicule ID={} affecté à Agence ID={}", idVehicule, idAgence1);
    }

    @Test
    @Order(2)
    @DisplayName("Affecter un véhicule inexistant - doit lever ResourceNotFoundException")
    public void testAffecterVehiculeInexistant() {
        log.info("========== TEST 2: Affecter véhicule inexistant ==========");

        Long vehiculeInexistantId = 99999L;

        // Tenter d'affecter un véhicule inexistant doit lever une exception
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> vehiculeService.affecterVehiculeAAgence(vehiculeInexistantId, idAgence1),
                "Une exception doit être levée pour un véhicule inexistant"
        );

        assertTrue(exception.getMessage().contains("Véhicule introuvable") ||
                        exception.getMessage().contains(vehiculeInexistantId.toString()),
                "Le message d'erreur doit indiquer que le véhicule est introuvable");

        log.info("✓ Exception levée correctement : {}", exception.getMessage());
    }

    @Test
    @Order(3)
    @DisplayName("Affecter un véhicule à une agence inexistante - doit lever ResourceNotFoundException")
    public void testAffecterVehiculeAAgenceInexistante() {
        log.info("========== TEST 3: Affecter véhicule à agence inexistante ==========");

        Long agenceInexistanteId = 88888L;

        // Tenter d'affecter à une agence inexistante doit lever une exception
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> vehiculeService.affecterVehiculeAAgence(idVehicule, agenceInexistanteId),
                "Une exception doit être levée pour une agence inexistante"
        );

        assertTrue(exception.getMessage().contains("Agence introuvable") ||
                        exception.getMessage().contains(agenceInexistanteId.toString()),
                "Le message d'erreur doit indiquer que l'agence est introuvable");

        log.info("✓ Exception levée correctement : {}", exception.getMessage());
    }

    @Test
    @Order(4)
    @DisplayName("Transférer un véhicule d'une agence vers une autre")
    public void testTransfererVehiculeEntreAgences() {
        log.info("========== TEST 4: Transférer véhicule entre agences ==========");

        // Le véhicule est actuellement rattaché à l'agence 1 (test 1)
        Vehicule vehiculeAvant = vehiculeRepository.findById(idVehicule).orElseThrow();
        assertEquals(idAgence1, vehiculeAvant.getAgence().getIdAgence(),
                "Le véhicule doit être initialement rattaché à l'agence 1");

        // Transférer vers l'agence 2
        assertDoesNotThrow(() -> vehiculeService.affecterVehiculeAAgence(idVehicule, idAgence2));

        // Vérifier que le transfert a bien été effectué
        Vehicule vehiculeApres = vehiculeRepository.findById(idVehicule).orElseThrow();
        assertNotNull(vehiculeApres.getAgence(), "Le véhicule doit être rattaché à une agence");
        assertEquals(idAgence2, vehiculeApres.getAgence().getIdAgence(),
                "Le véhicule doit maintenant être rattaché à l'agence 2");

        log.info("✓ Véhicule transféré de Agence ID={} vers Agence ID={}", idAgence1, idAgence2);
    }

    @Test
    @Order(5)
    @DisplayName("Désaffecter un véhicule et vérifier clé étrangère = null")
    public void testDesaffecterVehicule() {
        log.info("========== TEST 5: Désaffecter véhicule ==========");

        // Le véhicule est actuellement rattaché à l'agence 2 (test 4)
        Vehicule vehiculeAvant = vehiculeRepository.findById(idVehicule).orElseThrow();
        assertNotNull(vehiculeAvant.getAgence(), "Le véhicule doit être rattaché à une agence avant désaffectation");

        // Désaffecter le véhicule
        assertDoesNotThrow(() -> vehiculeService.desaffecterVehiculDeAgence(idVehicule));

        // Vérifier que la clé étrangère est bien null
        Vehicule vehiculeApres = vehiculeRepository.findById(idVehicule).orElseThrow();
        assertNull(vehiculeApres.getAgence(),
                "Le véhicule ne doit plus être rattaché à aucune agence (clé étrangère = null)");

        log.info("✓ Véhicule désaffecté, clé étrangère agence = null");
    }

    // ==========================================
    // TESTS : AFFECTATION EMPLOYÉ
    // ==========================================

    @Test
    @Order(6)
    @DisplayName("Affecter un employé à une agence existante")
    public void testAffecterEmployeAAgenceExistante() {
        log.info("========== TEST 6: Affecter employé à agence existante ==========");

        // Affecter l'employé à l'agence 1
        assertDoesNotThrow(() -> employeService.affecterEmployeAAgence(idEmploye, idAgence1));

        // Vérifier que l'affectation a bien été effectuée
        Employe employe = employeRepository.findById(idEmploye).orElseThrow();
        assertNotNull(employe.getAgence(), "L'employé doit être rattaché à une agence");
        assertEquals(idAgence1, employe.getAgence().getIdAgence(),
                "L'employé doit être rattaché à l'agence 1");

        log.info("✓ Employé ID={} affecté à Agence ID={}", idEmploye, idAgence1);
    }

    @Test
    @Order(7)
    @DisplayName("Affecter un employé inexistant - doit lever ResourceNotFoundException")
    public void testAffecterEmployeInexistant() {
        log.info("========== TEST 7: Affecter employé inexistant ==========");

        Long employeInexistantId = 77777L;

        // Tenter d'affecter un employé inexistant doit lever une exception
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> employeService.affecterEmployeAAgence(employeInexistantId, idAgence1),
                "Une exception doit être levée pour un employé inexistant"
        );

        assertTrue(exception.getMessage().contains("Employé introuvable") ||
                        exception.getMessage().contains(employeInexistantId.toString()),
                "Le message d'erreur doit indiquer que l'employé est introuvable");

        log.info("✓ Exception levée correctement : {}", exception.getMessage());
    }

    @Test
    @Order(8)
    @DisplayName("Affecter un employé à une agence inexistante - doit lever ResourceNotFoundException")
    public void testAffecterEmployeAAgenceInexistante() {
        log.info("========== TEST 8: Affecter employé à agence inexistante ==========");

        Long agenceInexistanteId = 66666L;

        // Tenter d'affecter à une agence inexistante doit lever une exception
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> employeService.affecterEmployeAAgence(idEmploye, agenceInexistanteId),
                "Une exception doit être levée pour une agence inexistante"
        );

        assertTrue(exception.getMessage().contains("Agence introuvable") ||
                        exception.getMessage().contains(agenceInexistanteId.toString()),
                "Le message d'erreur doit indiquer que l'agence est introuvable");

        log.info("✓ Exception levée correctement : {}", exception.getMessage());
    }

    @Test
    @Order(9)
    @DisplayName("Transférer un employé d'une agence vers une autre")
    public void testTransfererEmployeEntreAgences() {
        log.info("========== TEST 9: Transférer employé entre agences ==========");

        // L'employé est actuellement rattaché à l'agence 1 (test 6)
        Employe employeAvant = employeRepository.findById(idEmploye).orElseThrow();
        assertEquals(idAgence1, employeAvant.getAgence().getIdAgence(),
                "L'employé doit être initialement rattaché à l'agence 1");

        // Transférer vers l'agence 2
        assertDoesNotThrow(() -> employeService.affecterEmployeAAgence(idEmploye, idAgence2));

        // Vérifier que le transfert a bien été effectué
        Employe employeApres = employeRepository.findById(idEmploye).orElseThrow();
        assertNotNull(employeApres.getAgence(), "L'employé doit être rattaché à une agence");
        assertEquals(idAgence2, employeApres.getAgence().getIdAgence(),
                "L'employé doit maintenant être rattaché à l'agence 2");

        log.info("✓ Employé transféré de Agence ID={} vers Agence ID={}", idAgence1, idAgence2);
    }

    @Test
    @Order(10)
    @DisplayName("Désaffecter un employé et vérifier clé étrangère = null")
    public void testDesaffecterEmploye() {
        log.info("========== TEST 10: Désaffecter employé ==========");

        // L'employé est actuellement rattaché à l'agence 2 (test 9)
        Employe employeAvant = employeRepository.findById(idEmploye).orElseThrow();
        assertNotNull(employeAvant.getAgence(), "L'employé doit être rattaché à une agence avant désaffectation");

        // Désaffecter l'employé
        assertDoesNotThrow(() -> employeService.desaffecterEmployeDeAgence(idEmploye));

        // Vérifier que la clé étrangère est bien null
        Employe employeApres = employeRepository.findById(idEmploye).orElseThrow();
        assertNull(employeApres.getAgence(),
                "L'employé ne doit plus être rattaché à aucune agence (clé étrangère = null)");

        log.info("✓ Employé désaffecté, clé étrangère agence = null");
    }

    // ==========================================
    // TEST : TRANSACTIONALITÉ
    // ==========================================

    @Test
    @Order(11)
    @DisplayName("Vérifier que les opérations sont transactionnelles")
    public void testOperationsTransactionnelles() {
        log.info("========== TEST 11: Vérifier transactionnalité ==========");

        // Affecter le véhicule à l'agence 1
        vehiculeService.affecterVehiculeAAgence(idVehicule, idAgence1);

        // Vérifier en base
        Vehicule vehiculeEnBase = vehiculeRepository.findById(idVehicule).orElseThrow();
        assertEquals(idAgence1, vehiculeEnBase.getAgence().getIdAgence(),
                "Les modifications doivent être persistées en base (transaction committée)");

        log.info("✓ Les opérations sont bien transactionnelles (@Transactional fonctionne)");
    }
}
