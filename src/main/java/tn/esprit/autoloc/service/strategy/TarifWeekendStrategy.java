package tn.esprit.autoloc.service.strategy;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.Vehicule;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.temporal.ChronoUnit;

/**
 * Stratégie de tarification pour les weekends (≤ 3 jours incluant samedi/dimanche).
 * Applique une remise : 100€/jour au lieu du tarif standard.
 * 
 * @Component : Permet à Spring de gérer cette classe comme un bean
 */
@Component
public class TarifWeekendStrategy implements ITarificationStrategy {
    
    @Override
    public BigDecimal calculerTarif(Vehicule vehicule, Reservation reservation) {
        // Calculer le nombre de jours
        long nombreJours = ChronoUnit.DAYS.between(
            reservation.getDateDebut(), 
            reservation.getDateFin()
        );
        
        // Tarif weekend : 100€ par jour
        BigDecimal tarifJournalier = new BigDecimal("100.00");
        
        return tarifJournalier.multiply(new BigDecimal(nombreJours)).setScale(2, RoundingMode.HALF_UP);
    }
    
    @Override
    public String getCode() {
        return "WEEKEND";
    }
}
