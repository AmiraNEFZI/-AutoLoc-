package tn.esprit.autoloc;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.service.IClientService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour ClientService (TP 4 - Couche Service).
 * Teste la couche Service avec injection de dépendances et règles métier.
 */
@SpringBootTest
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClientServiceTest {

    @Autowired
    private IClientService clientService;

    private static Long idClientSauvegarde;

    @Test
    @Order(1)
    public void testCreateClient() {
        log.info("========== TEST 1: Créer un client ==========");

        Client client = new Client();
        client.setNom("Ben Ali");
        client.setPrenom("Mohamed");
        client.setEmail("mohamed.benali@example.com");
        client.setTelephone("+216 20 123 456");
        client.setNumPermis("P123456");
        client.setDateInscription(LocalDate.now());

        Client clientSauvegarde = clientService.create(client);

        assertNotNull(clientSauvegarde);
        assertNotNull(clientSauvegarde.getIdClient());
        assertEquals("Ben Ali", clientSauvegarde.getNom());

        log.info("✓ Client créé avec succès, ID = {}", clientSauvegarde.getIdClient());
        idClientSauvegarde = clientSauvegarde.getIdClient();
    }

    @Test
    @Order(2)
    public void testFindAllClients() {
        log.info("========== TEST 2: Lister tous les clients ==========");

        List<Client> clients = clientService.findAll();

        assertNotNull(clients);
        assertTrue(clients.size() > 0);

        log.info("✓ Nombre total de clients : {}", clients.size());
    }

    @Test
    @Order(3)
    public void testFindById() {
        log.info("========== TEST 3: Rechercher un client par ID ==========");

        Client client = clientService.findById(idClientSauvegarde);

        assertNotNull(client);
        assertEquals(idClientSauvegarde, client.getIdClient());
        assertEquals("Ben Ali", client.getNom());

        log.info("✓ Client trouvé : {} {}", client.getPrenom(), client.getNom());
    }

    @Test
    @Order(4)
    public void testUpdateClient() {
        log.info("========== TEST 4: Mettre à jour un client ==========");

        Client client = clientService.findById(idClientSauvegarde);
        client.setEmail("mohamed.benali.updated@example.com");
        client.setTelephone("+216 20 999 888");

        Client clientModifie = clientService.update(idClientSauvegarde, client);

        assertEquals("mohamed.benali.updated@example.com", clientModifie.getEmail());

        log.info("✓ Client mis à jour : {}", clientModifie.getEmail());
    }

    @Test
    @Order(5)
    public void testDeleteClient() {
        log.info("========== TEST 5: Supprimer un client ==========");

        clientService.deleteById(idClientSauvegarde);

        assertThrows(ResourceNotFoundException.class, () -> {
            clientService.findById(idClientSauvegarde);
        });

        log.info("✓ Client supprimé avec ID : {}", idClientSauvegarde);
    }

    @Test
    @Order(6)
    public void testCreateClientAvecIdNonNull() {
        log.info("========== TEST 6: Refuser création avec ID non null ==========");

        Client client = new Client();
        client.setIdClient(999L);
        client.setNom("Test");
        client.setEmail("test@example.com");

        assertThrows(IllegalArgumentException.class, () -> {
            clientService.create(client);
        });

        log.info("✓ Règle métier respectée : création refusée avec ID non null");
    }

    @Test
    @Order(7)
    public void testCreateClientSansNom() {
        log.info("========== TEST 7: Refuser création sans nom ==========");

        Client client = new Client();
        client.setEmail("test@example.com");

        assertThrows(IllegalArgumentException.class, () -> {
            clientService.create(client);
        });

        log.info("✓ Règle métier respectée : nom obligatoire");
    }

    @Test
    @Order(8)
    public void testCreateClientSansEmail() {
        log.info("========== TEST 8: Refuser création sans email ==========");

        Client client = new Client();
        client.setNom("Test");

        assertThrows(IllegalArgumentException.class, () -> {
            clientService.create(client);
        });

        log.info("✓ Règle métier respectée : email obligatoire");
    }
}
