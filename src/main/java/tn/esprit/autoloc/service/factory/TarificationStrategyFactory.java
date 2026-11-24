package tn.esprit.autoloc.service.factory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.service.strategy.ITarificationStrategy;

import java.time.DayOfWeek;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Factory Method pour sélectionner dynamiquement la stratégie de tarification
 * adaptée à une réservation.
 * 
 * Design Pattern : FACTORY METHOD (GoF Créationnel)
 * 
 * Principe SOLID respecté :
 * - SRP : Une seule responsabilité = décider quelle stratégie utiliser
 * - DIP : Dépend de l'interface ITarificationStrategy, pas des classes concrètes
 * - OCP : Pour ajouter une nouvelle stratégie, on modifie uniquement la logique de sélection
 * 
 * La factory injecte automatiquement toutes les implémentations de ITarificationStrategy
 * grâce à Spring (injection de List<ITarificationStrategy>).
 */
@Component
@RequiredArgsConstructor
public class TarificationStrategyFactory {
    
    // Spring injecte automatiquement TOUTES les implémentations de ITarificationStrategy
    // (TarifStandardStrategy, TarifWeekendStrategy, TarifLongueDureeStrategy)
    private final List<ITarificationStrategy> strategies;
    
    /**
     * Sélectionne dynamiquement la TarificationStrategy adaptée à une réservation.
     * Toutes les implémentations String de TarificationStrategy sont injectées automatiquement
     * (Map<code, strategy>).
     * 
     * Logique de sélection :
     * 1. Si durée >= 7 jours → TarifLongueDureeStrategy
     * 2. Si durée <= 3 jours ET weekend → TarifWeekendStrategy
     * 3. Sinon → TarifStandardStrategy (fallback)
     * 
     * @param reservation la réservation pour laquelle calculer le tarif
     * @return la stratégie appropriée
     */
    public ITarificationStrategy resoudre(Reservation reservation) {
        // Créer un Map<code, strategy> pour accès rapide
        Map<String, ITarificationStrategy> strategyMap = strategies.stream()
            .collect(Collectors.toMap(ITarificationStrategy::getCode, s -> s));
        
        // Calculer la durée de la réservation
        long nombreJours = ChronoUnit.DAYS.between(
            reservation.getDateDebut(),
            reservation.getDateFin()
        );
        
        // Règle 1 : Longue durée (>= 7 jours)
        if (nombreJours >= 7) {
            return strategyMap.get("LONGUE_DUREE");
        }
        
        // Règle 2 : Weekend (durée <= 3 jours ET weekend)
        if (nombreJours <= 3 && estWeekend(reservation)) {
            return strategyMap.get("WEEKEND");
        }
        
        // Règle 3 : Standard (fallback par défaut)
        return strategyMap.get("STANDARD");
    }
    
    /**
     * Vérifie si la réservation inclut un weekend (samedi ou dimanche).
     * 
     * @param reservation la réservation à vérifier
     * @return true si la réservation commence un vendredi, samedi ou dimanche
     */
    private boolean estWeekend(Reservation reservation) {
        if (reservation.getDateDebut() == null) {
            return false;
        }
        
        DayOfWeek jourDebut = reservation.getDateDebut().getDayOfWeek();
        
        // La réservation est considérée "weekend" si elle commence :
        // - Vendredi (pour weekend de 3 jours)
        // - Samedi
        // - Dimanche
        return jourDebut.equals(DayOfWeek.FRIDAY) 
            || jourDebut.equals(DayOfWeek.SATURDAY) 
            || jourDebut.equals(DayOfWeek.SUNDAY);
    }
}
