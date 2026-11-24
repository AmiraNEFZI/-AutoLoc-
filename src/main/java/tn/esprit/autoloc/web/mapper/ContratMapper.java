package tn.esprit.autoloc.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.web.dto.ContratDTO;

/**
 * Mapper MapStruct pour la conversion entre Contrat et ContratDTO.
 */
@Mapper(componentModel = "spring")
public interface ContratMapper {

    /**
     * Convertit une entité Contrat en ContratDTO.
     * 
     * @param contrat l'entité Contrat
     * @return le DTO ContratDTO
     */
    @Mapping(source = "idContrat", target = "id")
    @Mapping(source = "reservation.idReservation", target = "reservationId")
    ContratDTO toDto(Contrat contrat);

    /**
     * Convertit un ContratDTO en entité Contrat.
     * 
     * @param dto le DTO ContratDTO
     * @return l'entité Contrat
     */
    @Mapping(source = "id", target = "idContrat")
    @Mapping(target = "reservation", ignore = true)
    Contrat toEntity(ContratDTO dto);
}
