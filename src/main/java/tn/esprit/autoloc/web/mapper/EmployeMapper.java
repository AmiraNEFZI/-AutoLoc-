package tn.esprit.autoloc.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.web.dto.EmployeDTO;

/**
 * Mapper MapStruct pour la conversion entre Employe et EmployeDTO.
 */
@Mapper(componentModel = "spring")
public interface EmployeMapper {

    /**
     * Convertit une entité Employe en EmployeDTO.
     * 
     * @param employe l'entité Employe
     * @return le DTO EmployeDTO
     */
    @Mapping(source = "idEmploye", target = "id")
    @Mapping(source = "agence.idAgence", target = "agenceId")
    EmployeDTO toDto(Employe employe);

    /**
     * Convertit un EmployeDTO en entité Employe.
     * 
     * @param dto le DTO EmployeDTO
     * @return l'entité Employe
     */
    @Mapping(source = "id", target = "idEmploye")
    @Mapping(target = "agence", ignore = true)
    Employe toEntity(EmployeDTO dto);
}
