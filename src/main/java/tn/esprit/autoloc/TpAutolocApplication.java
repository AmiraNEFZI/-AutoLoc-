package tn.esprit.autoloc;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.domain.Maintenance;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.domain.enums.CategorieVehicule;
import tn.esprit.autoloc.domain.enums.StatutReservation;
import tn.esprit.autoloc.domain.enums.StatutVehicule;
import tn.esprit.autoloc.repository.IAgenceRepository;
import tn.esprit.autoloc.repository.IClientRepository;
import tn.esprit.autoloc.repository.IContratRepository;
import tn.esprit.autoloc.repository.IMaintenanceRepository;
import tn.esprit.autoloc.repository.IReservationRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
@RequiredArgsConstructor // Génère un constructeur avec les champs final (injection de dépendances)
@Slf4j // Génère automatiquement un logger
public class TpAutolocApplication {

    // Injection des repositories via constructeur
    private final IAgenceRepository agenceRepository;
    private final IClientRepository clientRepository;
    private final IVehiculeRepository vehiculeRepository;
    private final IContratRepository contratRepository;
    private final IReservationRepository reservationRepository;
    private final IMaintenanceRepository maintenanceRepository;

    public static void main(String[] args) {
        SpringApplication.run(TpAutolocApplication.class, args);
    }

    /**
     * Fonction de test pour les repositories.
     * S'exécute automatiquement après l'initialisation du contexte Spring.
     */
    @PostConstruct
    public void testRepositories() {
        testAgenceRepository();
        testClientRepository();
        testVehiculeRepository();
        testExerciceAutonome();
    }

    private void testAgenceRepository() {
        log.info("========== DÉBUT TEST AgenceRepository ==========");

        // 1. Créer une instance d'Agence
        Agence agence = new Agence();
        agence.setNom("Agence Tunis Centre");
        agence.setAdresse("Avenue Habib Bourguiba, Tunis");
        agence.setTelephone("+216 71 123 456");
        log.info("1. Agence créée : {}", agence);

        // 2. Enregistrer l'agence avec save()
        Agence agenceSauvegardee = agenceRepository.save(agence);
        log.info("2. Agence enregistrée avec ID : {}", agenceSauvegardee.getIdAgence());
        log.info("   Objet retourné par save() : {}", agenceSauvegardee);

        // 3. Afficher toutes les agences avec findAll()
        List<Agence> toutesLesAgences = agenceRepository.findAll();
        log.info("3. Nombre total d'agences : {}", toutesLesAgences.size());
        log.info("   Liste complète : {}", toutesLesAgences);

        // 4. Récupérer une agence avec findById()
        Long idRecherche = agenceSauvegardee.getIdAgence();
        Optional<Agence> agenceTrouvee = agenceRepository.findById(idRecherche);
        if (agenceTrouvee.isPresent()) {
            log.info("4. Agence trouvée avec ID {} : {}", idRecherche, agenceTrouvee.get());
        } else {
            log.warn("4. Aucune agence trouvée avec ID {}", idRecherche);
        }

        // 5. Vérifier l'existence avec existsById()
        boolean existe = agenceRepository.existsById(idRecherche);
        log.info("5. L'agence avec ID {} existe : {}", idRecherche, existe);

        // 6. Afficher le nombre d'agences avec count()
        long nombreAgences = agenceRepository.count();
        log.info("6. Nombre total d'agences dans la BD : {}", nombreAgences);

        // 7. Supprimer l'agence avec deleteById()
        agenceRepository.deleteById(idRecherche);
        log.info("7. Agence avec ID {} supprimée", idRecherche);

        // 8. Vérifier qu'elle n'existe plus
        boolean existeApresSupression = agenceRepository.existsById(idRecherche);
        log.info("8. L'agence avec ID {} existe après suppression : {}", idRecherche, existeApresSupression);

        log.info("========== FIN TEST AgenceRepository ==========\n");
    }

    private void testClientRepository() {
        log.info("========== DÉBUT TEST ClientRepository ==========");

        // 1. Créer deux clients avec les attributs réellement présents dans l'entité Client
        Client client1 = new Client();
        client1.setNom("Nefzi");
        client1.setPrenom("Amira");
        client1.setEmail("amira.nefzi@esprit.tn");
        client1.setTelephone("+216 98 123 456");
        log.info("1. Client 1 créé : {} {}", client1.getPrenom(), client1.getNom());

        Client client2 = new Client();
        client2.setNom("Ben Salem");
        client2.setPrenom("Mohamed");
        client2.setEmail("mohamed.bensalem@esprit.tn");
        client2.setTelephone("+216 22 654 321");
        log.info("   Client 2 créé : {} {}", client2.getPrenom(), client2.getNom());

        // 2. Enregistrer les deux clients avec save()
        Client client1Sauvegarde = clientRepository.save(client1);
        Client client2Sauvegarde = clientRepository.save(client2);
        log.info("2. Clients enregistrés avec IDs : {} et {}", 
                 client1Sauvegarde.getIdClient(), client2Sauvegarde.getIdClient());

        // 3. Afficher la liste avec findAll()
        List<Client> tousLesClients = clientRepository.findAll();
        log.info("3. Nombre total de clients : {}", tousLesClients.size());
        log.info("   Liste complète :");
        tousLesClients.forEach(c -> log.info("   - {} {} (ID: {})", c.getPrenom(), c.getNom(), c.getIdClient()));

        // 4. Rechercher un client avec findById()
        Long idRecherche = client1Sauvegarde.getIdClient();
        Optional<Client> clientTrouve = clientRepository.findById(idRecherche);
        if (clientTrouve.isPresent()) {
            Client c = clientTrouve.get();
            log.info("4. Client trouvé avec ID {} : {} {} - {}", 
                     idRecherche, c.getPrenom(), c.getNom(), c.getEmail());
        } else {
            log.warn("4. Aucun client trouvé avec ID {}", idRecherche);
        }

        // 5. Tester existsById() avec un ID existant
        boolean existe = clientRepository.existsById(idRecherche);
        log.info("5. Le client avec ID {} existe : {}", idRecherche, existe);

        // 6. Tester existsById() avec un ID inexistant
        Long idInexistant = 9999L;
        boolean existeInexistant = clientRepository.existsById(idInexistant);
        log.info("   Le client avec ID {} existe : {}", idInexistant, existeInexistant);

        // 7. Afficher le nombre de clients avec count()
        long nombreClients = clientRepository.count();
        log.info("6. Nombre total de clients dans la BD : {}", nombreClients);

        log.info("========== FIN TEST ClientRepository ==========\n");
    }

    private void testVehiculeRepository() {
        log.info("========== DÉBUT TEST VehiculeRepository ==========");

        // 1. Créer un véhicule avec les attributs de l'entité Vehicule
        Vehicule vehicule = new Vehicule();
        vehicule.setMarque("Peugeot");
        vehicule.setModele("308");
        vehicule.setImmatriculation("123 TUN 456");
        vehicule.setCategorie(CategorieVehicule.BERLINE);
        vehicule.setTarifJournalier(new BigDecimal("150.00"));
        vehicule.setStatut(StatutVehicule.DISPONIBLE);
        log.info("1. Véhicule créé : {} {} - {}", vehicule.getMarque(), vehicule.getModele(), 
                 vehicule.getImmatriculation());

        // 2. Enregistrer avec save()
        Vehicule vehiculeSauvegarde = vehiculeRepository.save(vehicule);
        log.info("2. Véhicule enregistré avec ID : {}", vehiculeSauvegarde.getIdVehicule());
        log.info("   Tarif journalier : {} TND", vehiculeSauvegarde.getTarifJournalier());

        // 3. Afficher tous les véhicules avec findAll()
        List<Vehicule> tousLesVehicules = vehiculeRepository.findAll();
        log.info("3. Nombre total de véhicules : {}", tousLesVehicules.size());
        log.info("   Liste complète :");
        tousLesVehicules.forEach(v -> log.info("   - {} {} (Immat: {}, Tarif: {} TND)", 
                                                v.getMarque(), v.getModele(), 
                                                v.getImmatriculation(), v.getTarifJournalier()));

        // 4. Afficher le nombre de véhicules avec count()
        long nombreVehicules = vehiculeRepository.count();
        log.info("4. Nombre total de véhicules dans la BD : {}", nombreVehicules);

        // 5. Modifier une propriété du véhicule puis utiliser save() pour enregistrer la modification
        log.info("5. Modification du véhicule ID {} :", vehiculeSauvegarde.getIdVehicule());
        log.info("   Tarif AVANT modification : {} TND", vehiculeSauvegarde.getTarifJournalier());
        
        vehiculeSauvegarde.setTarifJournalier(new BigDecimal("180.00"));
        vehiculeSauvegarde.setStatut(StatutVehicule.LOUE);
        
        Vehicule vehiculeModifie = vehiculeRepository.save(vehiculeSauvegarde);
        log.info("   Tarif APRÈS modification : {} TND", vehiculeModifie.getTarifJournalier());
        log.info("   Statut APRÈS modification : {}", vehiculeModifie.getStatut());
        log.info("   → save() a effectué un UPDATE (ID existant)");

        // 6. Supprimer le véhicule avec deleteById()
        Long idVehicule = vehiculeSauvegarde.getIdVehicule();
        vehiculeRepository.deleteById(idVehicule);
        log.info("6. Véhicule avec ID {} supprimé", idVehicule);

        // Vérifier la suppression
        boolean existeApresSupression = vehiculeRepository.existsById(idVehicule);
        log.info("   Le véhicule avec ID {} existe après suppression : {}", idVehicule, existeApresSupression);

        log.info("========== FIN TEST VehiculeRepository ==========\n");
    }

    /**
     * PARTIE 5 - Exercice autonome
     * Test de 3 repositories au choix : Contrat, Reservation, Maintenance
     */
    private void testExerciceAutonome() {
        log.info("========== DÉBUT EXERCICE AUTONOME ==========");
        log.info("Test de 3 repositories : ContratRepository, ReservationRepository, MaintenanceRepository\n");

        testContratRepository();
        testReservationRepository();
        testMaintenanceRepository();

        log.info("========== FIN EXERCICE AUTONOME ==========\n");
    }

    private void testContratRepository() {
        log.info("--- Test ContratRepository ---");

        // 1. Création avec save()
        Contrat contrat = new Contrat();
        contrat.setDateSignature(LocalDate.of(2026, 10, 15));
        contrat.setMontantTotal(new BigDecimal("750.00"));
        contrat.setValide(true);

        Contrat contratSauvegarde = contratRepository.save(contrat);
        log.info("1. Contrat créé avec ID : {}", contratSauvegarde.getIdContrat());
        log.info("   Montant total : {} TND", contratSauvegarde.getMontantTotal());
        log.info("   Valide : {}", contratSauvegarde.getValide());

        // 2. Lecture avec findAll()
        List<Contrat> tousLesContrats = contratRepository.findAll();
        log.info("2. Nombre total de contrats : {}", tousLesContrats.size());

        // 3. Vérification avec existsById()
        boolean existe = contratRepository.existsById(contratSauvegarde.getIdContrat());
        log.info("3. Le contrat avec ID {} existe : {}", contratSauvegarde.getIdContrat(), existe);

        // 4. Suppression avec deleteById()
        contratRepository.deleteById(contratSauvegarde.getIdContrat());
        log.info("4. Contrat avec ID {} supprimé\n", contratSauvegarde.getIdContrat());
    }

    private void testReservationRepository() {
        log.info("--- Test ReservationRepository ---");

        // Pour créer une Reservation, on a besoin d'un Client et d'un Vehicule
        // On va créer des entités temporaires pour ce test

        Client clientTemp = new Client();
        clientTemp.setNom("Dupont");
        clientTemp.setPrenom("Jean");
        clientTemp.setEmail("jean.dupont@test.tn");
        clientTemp.setTelephone("+216 99 888 777");
        clientTemp = clientRepository.save(clientTemp);

        Vehicule vehiculeTemp = new Vehicule();
        vehiculeTemp.setMarque("Renault");
        vehiculeTemp.setModele("Clio");
        vehiculeTemp.setImmatriculation("789 TUN 012");
        vehiculeTemp.setCategorie(CategorieVehicule.CITADINE);
        vehiculeTemp.setTarifJournalier(new BigDecimal("100.00"));
        vehiculeTemp.setStatut(StatutVehicule.DISPONIBLE);
        vehiculeTemp = vehiculeRepository.save(vehiculeTemp);

        // 1. Création avec save()
        Reservation reservation = new Reservation();
        reservation.setDateDebut(LocalDate.of(2026, 11, 1));
        reservation.setDateFin(LocalDate.of(2026, 11, 5));
        reservation.setStatut(StatutReservation.CONFIRMEE);
        reservation.setClient(clientTemp);
        reservation.setVehicule(vehiculeTemp);

        Reservation reservationSauvegardee = reservationRepository.save(reservation);
        log.info("1. Réservation créée avec ID : {}", reservationSauvegardee.getIdReservation());
        log.info("   Client : {} {}", clientTemp.getPrenom(), clientTemp.getNom());
        log.info("   Véhicule : {} {}", vehiculeTemp.getMarque(), vehiculeTemp.getModele());

        // 2. Lecture avec findById()
        Optional<Reservation> reservationTrouvee = reservationRepository.findById(reservationSauvegardee.getIdReservation());
        if (reservationTrouvee.isPresent()) {
            Reservation r = reservationTrouvee.get();
            log.info("2. Réservation trouvée : du {} au {}, Statut: {}", 
                     r.getDateDebut(), r.getDateFin(), r.getStatut());
        }

        // 3. Vérification avec existsById()
        boolean existe = reservationRepository.existsById(reservationSauvegardee.getIdReservation());
        log.info("3. La réservation avec ID {} existe : {}", reservationSauvegardee.getIdReservation(), existe);

        // 4. Suppression avec deleteById()
        reservationRepository.deleteById(reservationSauvegardee.getIdReservation());
        log.info("4. Réservation avec ID {} supprimée", reservationSauvegardee.getIdReservation());

        // Nettoyage : supprimer les entités temporaires
        clientRepository.deleteById(clientTemp.getIdClient());
        vehiculeRepository.deleteById(vehiculeTemp.getIdVehicule());
        log.info("   Nettoyage : Client et Véhicule temporaires supprimés\n");
    }

    private void testMaintenanceRepository() {
        log.info("--- Test MaintenanceRepository ---");

        // Pour créer une Maintenance, on a besoin d'un Vehicule
        Vehicule vehiculeTemp = new Vehicule();
        vehiculeTemp.setMarque("Ford");
        vehiculeTemp.setModele("Focus");
        vehiculeTemp.setImmatriculation("345 TUN 678");
        vehiculeTemp.setCategorie(CategorieVehicule.BERLINE);
        vehiculeTemp.setTarifJournalier(new BigDecimal("130.00"));
        vehiculeTemp.setStatut(StatutVehicule.MAINTENANCE);
        vehiculeTemp = vehiculeRepository.save(vehiculeTemp);

        // 1. Création avec save()
        Maintenance maintenance = new Maintenance();
        maintenance.setDateDebut(LocalDate.of(2026, 10, 10));
        maintenance.setDateFin(LocalDate.of(2026, 10, 12));
        maintenance.setDescription("Révision complète + changement pneus");
        maintenance.setVehicule(vehiculeTemp);

        Maintenance maintenanceSauvegardee = maintenanceRepository.save(maintenance);
        log.info("1. Maintenance créée avec ID : {}", maintenanceSauvegardee.getIdMaintenance());
        log.info("   Description : {}", maintenanceSauvegardee.getDescription());
        log.info("   Véhicule : {} {}", vehiculeTemp.getMarque(), vehiculeTemp.getModele());

        // 2. Lecture avec findAll()
        List<Maintenance> toutesLesMaintenances = maintenanceRepository.findAll();
        log.info("2. Nombre total de maintenances : {}", toutesLesMaintenances.size());

        // 3. Vérification avec existsById()
        boolean existe = maintenanceRepository.existsById(maintenanceSauvegardee.getIdMaintenance());
        log.info("3. La maintenance avec ID {} existe : {}", maintenanceSauvegardee.getIdMaintenance(), existe);

        // 4. Suppression avec deleteById()
        maintenanceRepository.deleteById(maintenanceSauvegardee.getIdMaintenance());
        log.info("4. Maintenance avec ID {} supprimée", maintenanceSauvegardee.getIdMaintenance());

        // Nettoyage
        vehiculeRepository.deleteById(vehiculeTemp.getIdVehicule());
        log.info("   Nettoyage : Véhicule temporaire supprimé\n");
    }

}
