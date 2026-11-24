package tn.esprit.autoloc.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.web.dto.ReservationResponseDTO;

/**
 * Mapper MapStruct pour la conversion entre Reservation et ReservationResponseDTO.
 * 
 * Ce mapper utilise des correspondances explicites pour mapper les données
 * provenant des associations Client et Vehicule selon les spécifications du TP.
 */
@Mapper(componentModel = "spring")
public interface ReservationMapper {

    /**
     * Convertit une entité Reservation en ReservationResponseDTO.
     * 
     * Les données de réponse contiennent des informations provenant des associations :
     * - clientId : l'ID du client
     * - clientNomComplet : prénom + nom du client (expression Java)
     * - vehiculeId : l'ID du véhicule
     * - vehiculeImmatriculation : l'immatriculation du véhicule
     * - tarifEstime : ignoré (sera calculé séparément)
     * 
     * @param reservation l'entité Reservation
     * @return le DTO ReservationResponseDTO
     */
    @Mapping(source = "idReservation", target = "id")
    @Mapping(target = "clientId", source = "client.idClient")
    @Mapping(target = "clientNomComplet", expression = "java(reservation.getClient() != null ? reservation.getClient().getPrenom() + \" \" + reservation.getClient().getNom() : \"\")")
    @Mapping(target = "vehiculeId", source = "vehicule.idVehicule")
    @Mapping(target = "vehiculeImmatriculation", source = "vehicule.immatriculation")
    @Mapping(target = "tarifEstime", ignore = true)
    ReservationResponseDTO toDto(Reservation reservation);
}
