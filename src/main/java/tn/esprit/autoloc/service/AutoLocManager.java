package tn.esprit.autoloc.service;

import lombok.extern.slf4j.Slf4j;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.Vehicule;

import java.math.BigDecimal;

/**
 * Classe problématique qui viole plusieurs principes SOLID.
 * Cette classe représente la première version de l'application développée rapidement.
 * 
 * PROBLÈMES :
 * - Viole SRP : trop de responsabilités (calcul prix, notifications, sauvegarde)
 * - Viole OCP : difficile d'ajouter de nouveaux types de tarifs sans modifier le code
 * - Code rigide et difficile à tester
 * - Couplage fort entre les différentes fonctionnalités
 */
@Slf4j
public class AutoLocManager {
    
    /**
     * Loue un véhicule selon le type de réservation.
     * Responsabilités mélangées : calcul prix + notifications + sauvegarde
     */
    public void louerVehicule(String typeReservation, int nombreJours, String clientEmail) {
        double prix = 0;
        
        // Calcul du prix selon le type de réservation
        // PROBLÈME : Si on ajoute un nouveau type, on doit MODIFIER cette méthode (viole OCP)
        if (typeReservation.equals("LongueDuree")) {
            prix = nombreJours * 80.0;
        } else if (typeReservation.equals("Weekend")) {
            prix = nombreJours * 100.0;
        } else if (typeReservation.equals("Standard")) {
            prix = nombreJours * 150.0;
        }
        
        log.info("Contrat créé");
        log.info("Prix total : {}", prix);
        
        // PROBLÈME : Notifications mélangées avec la logique métier
        envoyerEmail(clientEmail);
        enregistrerPaiement();
        sauvegarderContrat();
    }
    
    /**
     * Envoie un email au client.
     * PROBLÈME : Responsabilité de notification mélangée avec la gestion des véhicules
     */
    private void envoyerEmail(String email) {
        log.info("Email envoyé à {}", email);
    }
    
    /**
     * Sauvegarde le contrat.
     * PROBLÈME : Responsabilité de persistance mélangée avec le reste
     */
    private void sauvegarderContrat() {
        log.info("Contrat sauvegardé");
    }
    
    /**
     * Enregistre un paiement.
     * PROBLÈME : Encore une autre responsabilité
     */
    private void enregistrerPaiement() {
        log.info("Paiement enregistré");
    }
}
