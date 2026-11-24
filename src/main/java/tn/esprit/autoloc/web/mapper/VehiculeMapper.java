package tn.esprit.autoloc.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.web.dto.VehiculeDTO;

/**
 * Mapper MapStruct pour la conversion entre Vehicule et VehiculeDTO.
 */
@Mapper(componentModel = "spring")
public interface VehiculeMapper {

    /**
     * Convertit une entité Vehicule en VehiculeDTO.
     * 
     * @param vehicule l'entité Vehicule
     * @return le DTO VehiculeDTO
     */
    @Mapping(source = "idVehicule", target = "id")
    @Mapping(source = "agence.idAgence", target = "agenceId")
    VehiculeDTO toDto(Vehicule vehicule);

    /**
     * Convertit un VehiculeDTO en entité Vehicule.
     * 
     * @param dto le DTO VehiculeDTO
     * @return l'entité Vehicule
     */
    @Mapping(source = "id", target = "idVehicule")
    @Mapping(target = "agence", ignore = true)
    Vehicule toEntity(VehiculeDTO dto);
}
