package tn.esprit.autoloc.service.observer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Reservation;

/**
 * Observateur concret : notification par email (simulée via logs).
 * 
 * Design Pattern : OBSERVER (GoF Comportemental) - CONCRETE OBSERVER
 * 
 * Responsabilité : Envoyer des emails aux clients lors des événements de réservation.
 * Dans une vraie application, cette classe utiliserait JavaMailSender ou un service externe.
 * 
 * Principe SOLID respecté :
 * - SRP : Une seule responsabilité = envoyer des emails
 * - OCP : Cette classe peut être étendue ou remplacée sans modifier le reste du code
 * 
 * @Slf4j : Annotation Lombok qui génère automatiquement un logger SLF4J
 */
@Slf4j
@Component
public class EmailNotificationObserver implements IReservationObserver {
    
    @Override
    public void onReservationConfirmee(Reservation reservation) {
        // Simulation d'envoi d'email (dans une vraie app, utiliser JavaMailSender)
        log.info("[EMAIL] Confirmation envoyée au client {} pour la réservation #{}",
                reservation.getClient() != null ? reservation.getClient().getEmail() : "inconnu",
                reservation.getIdReservation());
        
        // Exemple de contenu d'email :
        // Sujet : Confirmation de votre réservation
        // Corps : Bonjour, votre réservation du XX au XX est confirmée...
    }
    
    @Override
    public void onReservationAnnulee(Reservation reservation) {
        // Simulation d'envoi d'email d'annulation
        log.info("[EMAIL] Annulation notifiée au client {} pour la réservation #{}",
                reservation.getClient() != null ? reservation.getClient().getEmail() : "inconnu",
                reservation.getIdReservation());
        
        // Exemple de contenu d'email :
        // Sujet : Annulation de votre réservation
        // Corps : Bonjour, votre réservation du XX au XX a été annulée...
    }
}
