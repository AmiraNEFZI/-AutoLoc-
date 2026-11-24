package tn.esprit.autoloc.service.factory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tn.esprit.autoloc.service.observer.EmailNotificationObserver;
import tn.esprit.autoloc.service.observer.IReservationObserver;
import tn.esprit.autoloc.service.observer.SmsNotificationObserver;

/**
 * Factory Method pour créer les observateurs de notification.
 * 
 * Design Pattern : FACTORY METHOD (GoF Créationnel)
 * 
 * Objectif : Permet d'obtenir le bon canal de notification (email, sms, ...)
 * SANS exposer les classes concrètes au code appelant.
 * 
 * Principe SOLID respecté :
 * - SRP : Une seule responsabilité = créer des observateurs
 * - DIP : Le code appelant dépend de IReservationObserver, pas des implémentations
 * - OCP : Pour ajouter un nouveau canal, on ajoute un cas dans la factory
 * 
 * Note : Cette factory utilise des beans Spring injectés (EmailNotificationObserver, SmsNotificationObserver)
 * au lieu de faire des "new" en dur, ce qui facilite les tests et l'injection de dépendances.
 */
@Component
@RequiredArgsConstructor
public class NotificationFactory {
    
    // Injection des observateurs concrets (gérés par Spring)
    private final EmailNotificationObserver emailNotificationObserver;
    private final SmsNotificationObserver smsNotificationObserver;
    
    /**
     * Crée un observateur selon le canal de notification demandé.
     * 
     * @param canal le type de canal : "email", "sms", etc.
     * @return l'observateur correspondant
     * @throws IllegalArgumentException si le canal est inconnu
     */
    public IReservationObserver creerObservateur(String canal) {
        if (canal == null) {
            throw new IllegalArgumentException("Canal de notification inconnu : null. Canaux supportés : email, sms");
        }
        
        return switch (canal.toLowerCase()) {
            case "email" -> emailNotificationObserver;
            case "sms" -> smsNotificationObserver;
            default -> throw new IllegalArgumentException(
                "Canal de notification inconnu : " + canal + ". Canaux supportés : email, sms"
            );
        };
    }
}
