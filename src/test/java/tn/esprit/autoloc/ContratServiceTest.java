package tn.esprit.autoloc;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.service.IContratService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour ContratService (TP 4 - Couche Service).
 */
@SpringBootTest
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ContratServiceTest {

    @Autowired
    private IContratService contratService;

    private static Long idContratSauvegarde;

    @Test
    @Order(1)
    public void testCreateContrat() {
        log.info("========== TEST 1: Créer un contrat ==========");

        Contrat contrat = new Contrat();
        contrat.setDateSignature(LocalDate.now());
        contrat.setMontantTotal(new BigDecimal("560.00"));
        contrat.setValide(true);

        Contrat contratSauvegarde = contratService.create(contrat);

        assertNotNull(contratSauvegarde);
        assertNotNull(contratSauvegarde.getIdContrat());
        assertEquals(new BigDecimal("560.00"), contratSauvegarde.getMontantTotal());

        log.info("✓ Contrat créé avec succès, ID = {}", contratSauvegarde.getIdContrat());
        idContratSauvegarde = contratSauvegarde.getIdContrat();
    }

    @Test
    @Order(2)
    public void testFindAllContrats() {
        log.info("========== TEST 2: Lister tous les contrats ==========");

        List<Contrat> contrats = contratService.findAll();

        assertNotNull(contrats);
        assertTrue(contrats.size() > 0);

        log.info("✓ Nombre total de contrats : {}", contrats.size());
    }

    @Test
    @Order(3)
    public void testFindById() {
        log.info("========== TEST 3: Rechercher un contrat par ID ==========");

        Contrat contrat = contratService.findById(idContratSauvegarde);

        assertNotNull(contrat);
        assertEquals(new BigDecimal("560.00"), contrat.getMontantTotal());

        log.info("✓ Contrat trouvé avec montant : {}", contrat.getMontantTotal());
    }

    @Test
    @Order(4)
    public void testUpdateContrat() {
        log.info("========== TEST 4: Mettre à jour un contrat ==========");

        Contrat contrat = contratService.findById(idContratSauvegarde);
        contrat.setMontantTotal(new BigDecimal("650.00"));
        contrat.setValide(false);

        Contrat contratModifie = contratService.update(idContratSauvegarde, contrat);

        assertEquals(new BigDecimal("650.00"), contratModifie.getMontantTotal());
        assertFalse(contratModifie.getValide());

        log.info("✓ Contrat mis à jour avec nouveau montant : {}", contratModifie.getMontantTotal());
    }

    @Test
    @Order(5)
    public void testDeleteContrat() {
        log.info("========== TEST 5: Supprimer un contrat ==========");

        contratService.deleteById(idContratSauvegarde);

        assertThrows(ResourceNotFoundException.class, () -> {
            contratService.findById(idContratSauvegarde);
        });

        log.info("✓ Contrat supprimé avec ID : {}", idContratSauvegarde);
    }

    @Test
    @Order(6)
    public void testCreateContratAvecIdNonNull() {
        log.info("========== TEST 6: Refuser création avec ID non null ==========");

        Contrat contrat = new Contrat();
        contrat.setIdContrat(999L);
        contrat.setDateSignature(LocalDate.now());
        contrat.setMontantTotal(new BigDecimal("100.00"));
        contrat.setValide(true);

        assertThrows(IllegalArgumentException.class, () -> {
            contratService.create(contrat);
        });

        log.info("✓ Règle métier respectée : création refusée avec ID non null");
    }
}
