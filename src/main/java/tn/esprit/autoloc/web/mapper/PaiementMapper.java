package tn.esprit.autoloc.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.web.dto.PaiementDTO;

/**
 * Mapper MapStruct pour la conversion entre Paiement et PaiementDTO.
 */
@Mapper(componentModel = "spring")
public interface PaiementMapper {

    /**
     * Convertit une entité Paiement en PaiementDTO.
     * 
     * @param paiement l'entité Paiement
     * @return le DTO PaiementDTO
     */
    @Mapping(source = "idPaiement", target = "id")
    @Mapping(source = "contrat.idContrat", target = "contratId")
    PaiementDTO toDto(Paiement paiement);

    /**
     * Convertit un PaiementDTO en entité Paiement.
     * 
     * @param dto le DTO PaiementDTO
     * @return l'entité Paiement
     */
    @Mapping(source = "id", target = "idPaiement")
    @Mapping(target = "contrat", ignore = true)
    Paiement toEntity(PaiementDTO dto);
}
