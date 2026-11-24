package tn.esprit.autoloc.service.strategy;

import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.Vehicule;

import java.math.BigDecimal;

/**
 * Interface Strategy pour le calcul de tarification.
 * 
 * Design Pattern : STRATEGY (GoF Comportemental)
 * 
 * Principe SOLID respecté : 
 * - OCP (Open/Closed) : On peut ajouter de nouvelles stratégies sans modifier le code existant
 * - SRP : Chaque stratégie a une seule responsabilité : calculer un tarif spécifique
 * 
 * Utilisation : Permet de sélectionner dynamiquement l'algorithme de calcul du tarif
 * selon le contexte (type de réservation, durée, jour de la semaine...)
 */
public interface ITarificationStrategy {
    
    /**
     * Calcule le tarif pour une réservation donnée.
     * 
     * @param vehicule le véhicule loué
     * @param reservation la réservation concernée
     * @return le montant total à payer
     */
    BigDecimal calculerTarif(Vehicule vehicule, Reservation reservation);
    
    /**
     * Retourne un code identifiant la stratégie (pour la factory et les logs).
     * Exemples : "STANDARD", "WEEKEND", "LONGUE_DUREE"
     * 
     * @return le code de la stratégie
     */
    String getCode();
}
