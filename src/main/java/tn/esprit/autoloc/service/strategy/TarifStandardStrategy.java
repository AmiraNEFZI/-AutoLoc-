package tn.esprit.autoloc.service.strategy;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.Vehicule;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.temporal.ChronoUnit;

/**
 * Stratégie de tarification standard (par défaut).
 * Applique un tarif normal : 150€/jour.
 * Utilisée pour les réservations de durée moyenne (< 7 jours) en semaine.
 * 
 * @Component : Permet à Spring de gérer cette classe comme un bean
 */
@Component
public class TarifStandardStrategy implements ITarificationStrategy {
    
    @Override
    public BigDecimal calculerTarif(Vehicule vehicule, Reservation reservation) {
        // Calculer le nombre de jours
        long nombreJours = ChronoUnit.DAYS.between(
            reservation.getDateDebut(), 
            reservation.getDateFin()
        );
        
        // Tarif standard : 150€ par jour
        BigDecimal tarifJournalier = new BigDecimal("150.00");
        
        return tarifJournalier.multiply(new BigDecimal(nombreJours)).setScale(2, RoundingMode.HALF_UP);
    }
    
    @Override
    public String getCode() {
        return "STANDARD";
    }
}
