package tn.esprit.autoloc.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.web.dto.AgenceDTO;

/**
 * Mapper MapStruct pour la conversion entre Agence et AgenceDTO.
 * L'annotation @Mapper avec componentModel = "spring" permet l'injection automatique.
 */
@Mapper(componentModel = "spring")
public interface AgenceMapper {

    /**
     * Convertit une entité Agence en AgenceDTO.
     * 
     * @param agence l'entité Agence
     * @return le DTO AgenceDTO
     */
    @Mapping(source = "idAgence", target = "id")
    AgenceDTO toDto(Agence agence);

    /**
     * Convertit un AgenceDTO en entité Agence.
     * 
     * @param dto le DTO AgenceDTO
     * @return l'entité Agence
     */
    @Mapping(source = "id", target = "idAgence")
    Agence toEntity(AgenceDTO dto);
}
