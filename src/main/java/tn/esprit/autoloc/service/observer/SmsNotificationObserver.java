package tn.esprit.autoloc.service.observer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Reservation;

/**
 * Observateur concret : notification par SMS (simulée via logs).
 * 
 * Design Pattern : OBSERVER (GoF Comportemental) - CONCRETE OBSERVER
 * 
 * Responsabilité : Envoyer des SMS aux clients lors des événements de réservation.
 * Dans une vraie application, cette classe utiliserait une API SMS (Twilio, Nexmo...).
 * 
 * Principe SOLID respecté :
 * - SRP : Une seule responsabilité = envoyer des SMS
 * - OCP : Cette classe peut être étendue ou remplacée sans modifier le reste du code
 * 
 * @Slf4j : Annotation Lombok qui génère automatiquement un logger SLF4J
 */
@Slf4j
@Component
public class SmsNotificationObserver implements IReservationObserver {
    
    @Override
    public void onReservationConfirmee(Reservation reservation) {
        // Simulation d'envoi de SMS (dans une vraie app, utiliser une API SMS)
        log.info("[SMS] Confirmation envoyée au {} pour la réservation #{}",
                reservation.getClient() != null ? reservation.getClient().getTelephone() : "inconnu",
                reservation.getIdReservation());
        
        // Exemple de contenu SMS :
        // "AutoLoc : Votre réservation est confirmée. Rendez-vous le XX/XX à l'agence."
    }
    
    @Override
    public void onReservationAnnulee(Reservation reservation) {
        // Simulation d'envoi de SMS d'annulation
        log.info("[SMS] Annulation notifiée au {} pour la réservation #{}",
                reservation.getClient() != null ? reservation.getClient().getTelephone() : "inconnu",
                reservation.getIdReservation());
        
        // Exemple de contenu SMS :
        // "AutoLoc : Votre réservation a été annulée. Contactez-nous pour plus d'infos."
    }
}
