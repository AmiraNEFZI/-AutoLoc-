package tn.esprit.autoloc.service.strategy;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.Vehicule;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.temporal.ChronoUnit;

/**
 * Stratégie de tarification pour les longues durées (≥ 7 jours).
 * Applique une remise importante : 80€/jour au lieu du tarif standard.
 * 
 * @Component : Permet à Spring de gérer cette classe comme un bean
 */
@Component
public class TarifLongueDureeStrategy implements ITarificationStrategy {
    
    @Override
    public BigDecimal calculerTarif(Vehicule vehicule, Reservation reservation) {
        // Calculer le nombre de jours entre dateDebut et dateFin
        long nombreJours = ChronoUnit.DAYS.between(
            reservation.getDateDebut(), 
            reservation.getDateFin()
        );
        
        // Tarif longue durée : 80€ par jour
        BigDecimal tarifJournalier = new BigDecimal("80.00");
        
        return tarifJournalier.multiply(new BigDecimal(nombreJours)).setScale(2, RoundingMode.HALF_UP);
    }
    
    @Override
    public String getCode() {
        return "LONGUE_DUREE";
    }
}
