package tn.esprit.autoloc;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.repository.IClientRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour ClientRepository (TP 3 - Couche Repository).
 */
@SpringBootTest
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClientRepositoryTest {

    @Autowired
    private IClientRepository clientRepository;

    private static Long idClientSauvegarde;

    @Test
    @Order(1)
    public void testSaveClient() {
        log.info("========== TEST 1: Save Client ==========");

        Client client = new Client();
        client.setNom("Ben Ali");
        client.setPrenom("Mohamed");
        client.setEmail("mohamed.benali@example.com");
        client.setTelephone("+216 20 123 456");
        client.setNumPermis("P123456");
        client.setDateInscription(LocalDate.now());

        Client savedClient = clientRepository.save(client);

        assertNotNull(savedClient);
        assertNotNull(savedClient.getIdClient());
        assertEquals("Ben Ali", savedClient.getNom());
        assertEquals("mohamed.benali@example.com", savedClient.getEmail());

        log.info("✓ Client sauvegardé avec ID: {}", savedClient.getIdClient());
        idClientSauvegarde = savedClient.getIdClient();
    }

    @Test
    @Order(2)
    public void testFindAll() {
        log.info("========== TEST 2: FindAll Clients ==========");

        List<Client> clients = (List<Client>) clientRepository.findAll();

        assertNotNull(clients);
        assertTrue(clients.size() > 0);

        log.info("✓ Nombre total de clients: {}", clients.size());
    }

    @Test
    @Order(3)
    public void testFindById() {
        log.info("========== TEST 3: FindById Client ==========");

        Optional<Client> optionalClient = clientRepository.findById(idClientSauvegarde);

        assertTrue(optionalClient.isPresent());
        assertEquals("Ben Ali", optionalClient.get().getNom());

        log.info("✓ Client trouvé: {} {}", optionalClient.get().getPrenom(), optionalClient.get().getNom());
    }

    @Test
    @Order(4)
    public void testUpdateClient() {
        log.info("========== TEST 4: Update Client ==========");

        Client client = clientRepository.findById(idClientSauvegarde).orElseThrow();
        client.setEmail("mohamed.benali.updated@example.com");
        client.setTelephone("+216 20 999 888");

        Client updatedClient = clientRepository.save(client);

        assertEquals("mohamed.benali.updated@example.com", updatedClient.getEmail());
        assertEquals("+216 20 999 888", updatedClient.getTelephone());

        log.info("✓ Client mis à jour: {}", updatedClient.getEmail());
    }

    @Test
    @Order(5)
    public void testDeleteClient() {
        log.info("========== TEST 5: Delete Client ==========");

        clientRepository.deleteById(idClientSauvegarde);

        Optional<Client> deletedClient = clientRepository.findById(idClientSauvegarde);
        assertFalse(deletedClient.isPresent());

        log.info("✓ Client supprimé avec ID: {}", idClientSauvegarde);
    }

    @Test
    @Order(6)
    public void testCount() {
        log.info("========== TEST 6: Count Clients ==========");

        long count = clientRepository.count();

        assertTrue(count >= 0);
        log.info("✓ Nombre de clients: {}", count);
    }
}
