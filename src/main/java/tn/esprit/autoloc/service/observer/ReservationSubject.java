package tn.esprit.autoloc.service.observer;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Reservation;

import java.util.ArrayList;
import java.util.List;

/**
 * Sujet observable (GoF Observer) : maintient la liste des observateurs
 * et les notifie lors des changements d'état d'une réservation.
 * 
 * Design Pattern : OBSERVER (GoF Comportemental) - SUBJECT
 * 
 * Responsabilité : 
 * - Maintenir la liste des observateurs inscrits
 * - Notifier tous les observateurs lors d'événements (confirmation, annulation)
 * 
 * Principe SOLID respecté :
 * - SRP : Une seule responsabilité = gérer les observateurs et les notifier
 * - OCP : On peut ajouter/retirer des observateurs dynamiquement sans modifier le code
 * - DIP : Dépend de l'interface IReservationObserver, pas des implémentations concrètes
 * 
 * Analogie : 
 * C'est comme une chaîne YouTube : les abonnés (observateurs) sont notifiés
 * automatiquement quand une nouvelle vidéo (événement) est publiée.
 */
@Component
public class ReservationSubject {
    
    // Liste des observateurs inscrits
    // Tous les beans ReservationObserver de Spring sont injectés automatiquement
    private final List<IReservationObserver> observers;
    
    /**
     * Constructeur : initialise la liste des observateurs.
     * Spring injectera automatiquement tous les beans qui implémentent IReservationObserver.
     * 
     * @param observers liste des observateurs injectés par Spring
     */
    public ReservationSubject(List<IReservationObserver> observers) {
        this.observers = new ArrayList<>(observers);
    }
    
    /**
     * Notifie tous les observateurs qu'une réservation a été confirmée.
     * Parcourt la liste et appelle onReservationConfirmee() sur chaque observateur.
     * 
     * @param reservation la réservation confirmée
     */
    public void notifierConfirmation(Reservation reservation) {
        observers.forEach(observer -> observer.onReservationConfirmee(reservation));
    }
    
    /**
     * Notifie tous les observateurs qu'une réservation a été annulée.
     * Parcourt la liste et appelle onReservationAnnulee() sur chaque observateur.
     * 
     * @param reservation la réservation annulée
     */
    public void notifierAnnulation(Reservation reservation) {
        observers.forEach(observer -> observer.onReservationAnnulee(reservation));
    }
}
