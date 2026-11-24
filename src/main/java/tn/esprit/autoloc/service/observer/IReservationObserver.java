package tn.esprit.autoloc.service.observer;

import tn.esprit.autoloc.domain.Reservation;

/**
 * Interface Observer pour le pattern Observer.
 * 
 * Design Pattern : OBSERVER (GoF Comportemental)
 * 
 * Objectif : Interface commune à tous les observateurs notifiés lors des événements
 * du cycle de vie d'une réservation (confirmation, annulation...).
 * 
 * Principe SOLID respecté :
 * - ISP (Interface Segregation Principle) : Interface focalisée uniquement sur les notifications
 * - DIP : Le sujet (ReservationSubject) dépend de cette interface, pas des implémentations
 * - OCP : Pour ajouter un nouveau type de notification, on crée une nouvelle classe
 *         qui implémente cette interface, sans modifier le code existant
 * 
 * Cas d'usage :
 * - EmailNotificationObserver : notifie par email
 * - SmsNotificationObserver : notifie par SMS
 * - AdminNotificationObserver : notifie l'admin
 * - StatistiquesObserver : met à jour les statistiques
 */
public interface IReservationObserver {
    
    /**
     * Appelée quand une réservation est confirmée.
     * L'observateur peut envoyer un email, un SMS, mettre à jour des statistiques, etc.
     * 
     * @param reservation la réservation qui vient d'être confirmée
     */
    void onReservationConfirmee(Reservation reservation);
    
    /**
     * Appelée quand une réservation est annulée.
     * L'observateur peut notifier le client, l'admin, mettre à jour les stats, etc.
     * 
     * @param reservation la réservation qui vient d'être annulée
     */
    void onReservationAnnulee(Reservation reservation);
}
