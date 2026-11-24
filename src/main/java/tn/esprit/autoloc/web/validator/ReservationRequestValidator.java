package tn.esprit.autoloc.web.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import tn.esprit.autoloc.web.dto.ReservationRequestDTO;

import java.time.LocalDate;

/**
 * Validator personnalisé pour ReservationRequestDTO.
 * Vérifie la cohérence des dates de réservation.
 */
@Component
public class ReservationRequestValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return ReservationRequestDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ReservationRequestDTO dto = (ReservationRequestDTO) target;

        // Vérifier que les dates ne sont pas nulles
        if (dto.getDateDebut() == null) {
            errors.rejectValue("dateDebut", "dateDebut.null", "La date de début ne peut pas être nulle");
            return;
        }

        if (dto.getDateFin() == null) {
            errors.rejectValue("dateFin", "dateFin.null", "La date de fin ne peut pas être nulle");
            return;
        }

        LocalDate today = LocalDate.now();

        // Vérifier que la date de début est aujourd'hui ou dans le futur
        if (dto.getDateDebut().isBefore(today)) {
            errors.rejectValue("dateDebut", "dateDebut.past", 
                "La date de début doit être aujourd'hui ou dans le futur");
        }

        // Vérifier que la date de fin est après la date de début
        if (!dto.getDateFin().isAfter(dto.getDateDebut())) {
            errors.rejectValue("dateFin", "dateFin.invalid", 
                "La date de fin doit être après la date de début");
        }
    }
}
