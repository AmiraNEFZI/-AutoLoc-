package tn.esprit.autoloc;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.service.IAgenceService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour AgenceService.
 * Teste la couche Service avec injection de dépendances et règles métier.
 */
@SpringBootTest
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AgenceServiceTest {

    @Autowired
    private IAgenceService agenceService;

    private static Long idAgenceSauvegardee;

    @Test
    @Order(1)
    public void testCreateAgence() {
        log.info("========== TEST 1: Créer une agence ==========");

        // Créer une agence
        Agence agence = new Agence();
        agence.setNom("Agence Tunis Centre");
        agence.setAdresse("Avenue Habib Bourguiba, Tunis");
        agence.setTelephone("+216 71 123 456");
        agence.setVille("Tunis");

        log.info("Agence créée en mémoire : {}", agence.getNom());

        // Sauvegarder via le service
        Agence agenceSauvegardee = agenceService.create(agence);

        // Vérifications
        assertNotNull(agenceSauvegardee, "L'agence sauvegardée ne doit pas être null");
        assertNotNull(agenceSauvegardee.getIdAgence(), "L'ID doit être généré");
        assertEquals("Agence Tunis Centre", agenceSauvegardee.getNom());
        assertEquals("Tunis", agenceSauvegardee.getVille());

        log.info("✓ Agence créée avec succès, ID = {}", agenceSauvegardee.getIdAgence());

        // Sauvegarder l'ID pour les tests suivants
        idAgenceSauvegardee = agenceSauvegardee.getIdAgence();
    }

    @Test
    @Order(2)
    public void testFindAllAgences() {
        log.info("========== TEST 2: Lister toutes les agences ==========");

        List<Agence> agences = agenceService.findAll();

        assertNotNull(agences, "La liste ne doit pas être null");
        assertTrue(agences.size() > 0, "Il doit y avoir au moins une agence");

        log.info("✓ Nombre total d'agences : {}", agences.size());
        agences.forEach(a -> log.info("  - {} (ID: {}, Ville: {})",
                a.getNom(), a.getIdAgence(), a.getVille()));
    }

    @Test
    @Order(3)
    public void testFindById() {
        log.info("========== TEST 3: Rechercher une agence par ID ==========");

        assertNotNull(idAgenceSauvegardee, "L'ID de l'agence doit exister");

        Agence agence = agenceService.findById(idAgenceSauvegardee);

        assertNotNull(agence, "L'agence trouvée ne doit pas être null");
        assertEquals(idAgenceSauvegardee, agence.getIdAgence());
        assertEquals("Agence Tunis Centre", agence.getNom());

        log.info("✓ Agence trouvée : {} - {}", agence.getNom(), agence.getAdresse());
    }

    @Test
    @Order(4)
    public void testUpdateAgence() {
        log.info("========== TEST 4: Mettre à jour une agence ==========");

        assertNotNull(idAgenceSauvegardee, "L'ID de l'agence doit exister");

        // Récupérer l'agence existante
        Agence agence = agenceService.findById(idAgenceSauvegardee);

        // Modifier ses attributs
        agence.setNom("Agence Tunis Modernisée");
        agence.setVille("Tunis Capitale");

        // Mettre à jour via le service
        Agence agenceModifiee = agenceService.update(idAgenceSauvegardee, agence);

        assertNotNull(agenceModifiee, "L'agence modifiée ne doit pas être null");
        assertEquals("Agence Tunis Modernisée", agenceModifiee.getNom());
        assertEquals("Tunis Capitale", agenceModifiee.getVille());

        log.info("✓ Agence mise à jour : {} - {}", agenceModifiee.getNom(), agenceModifiee.getVille());
    }

    @Test
    @Order(5)
    public void testDeleteAgence() {
        log.info("========== TEST 5: Supprimer une agence ==========");

        assertNotNull(idAgenceSauvegardee, "L'ID de l'agence doit exister");

        // Supprimer l'agence
        agenceService.deleteById(idAgenceSauvegardee);

        log.info("✓ Agence avec ID {} supprimée", idAgenceSauvegardee);

        // Vérifier qu'elle n'existe plus (doit lever une exception)
        assertThrows(ResourceNotFoundException.class, () -> {
            agenceService.findById(idAgenceSauvegardee);
        }, "Une ResourceNotFoundException doit être levée");

        log.info("✓ Vérification OK : L'agence n'existe plus");
    }

    @Test
    @Order(6)
    public void testCreateAgenceAvecIdNonNull() {
        log.info("========== TEST 6: Refuser création avec ID non null ==========");

        Agence agence = new Agence();
        agence.setIdAgence(999L); // ID déjà défini
        agence.setNom("Agence Test");

        // Doit lever IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            agenceService.create(agence);
        }, "Une IllegalArgumentException doit être levée");

        log.info("✓ Règle métier respectée : création refusée avec ID non null");
    }

    @Test
    @Order(7)
    public void testCreateAgenceSansNom() {
        log.info("========== TEST 7: Refuser création sans nom ==========");

        Agence agence = new Agence();
        agence.setAdresse("123 Rue Test");
        // Pas de nom défini

        // Doit lever IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            agenceService.create(agence);
        }, "Une IllegalArgumentException doit être levée");

        log.info("✓ Règle métier respectée : nom obligatoire");
    }
}
