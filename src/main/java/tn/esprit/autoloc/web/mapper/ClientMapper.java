package tn.esprit.autoloc.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.web.dto.ClientDTO;

/**
 * Mapper MapStruct pour la conversion entre Client et ClientDTO.
 */
@Mapper(componentModel = "spring")
public interface ClientMapper {

    /**
     * Convertit une entité Client en ClientDTO.
     * 
     * @param client l'entité Client
     * @return le DTO ClientDTO
     */
    @Mapping(source = "idClient", target = "id")
    ClientDTO toDto(Client client);

    /**
     * Convertit un ClientDTO en entité Client.
     * 
     * @param dto le DTO ClientDTO
     * @return l'entité Client
     */
    @Mapping(source = "id", target = "idClient")
    Client toEntity(ClientDTO dto);
}
