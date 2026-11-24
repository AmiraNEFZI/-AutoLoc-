package tn.esprit.autoloc.service;

import java.util.List;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.web.dto.EmployeDTO;

/**
 * Interface du service pour l'entité Employe.
 * Expose les opérations CRUD métier en utilisant des DTO.
 */
public interface IEmployeService {

    /**
     * Créer un nouvel employé.
     * 
     * @param dto Le DTO EmployeDTO à créer
     * @return Le DTO EmployeDTO créé avec son ID généré
     * @throws IllegalArgumentException si l'id n'est pas null ou si un attribut obligatoire est vide
     */
    EmployeDTO creer(EmployeDTO dto);

    /**
     * Rechercher un employé par son identifiant.
     * 
     * @param id L'identifiant de l'employé
     * @return Le DTO EmployeDTO trouvé
     * @throws ResourceNotFoundException si l'employé n'existe pas
     */
    EmployeDTO obtenirParId(Long id);

    /**
     * Récupérer tous les employés.
     * 
     * @return La liste de tous les DTO EmployeDTO
     */
    List<EmployeDTO> listerTout();

    /**
     * Mettre à jour un employé existant.
     * 
     * @param id L'identifiant de l'employé à modifier
     * @param dto Le DTO EmployeDTO avec les nouvelles valeurs
     * @return Le DTO EmployeDTO mis à jour
     * @throws ResourceNotFoundException si l'employé n'existe pas
     */
    EmployeDTO modifier(Long id, EmployeDTO dto);

    /**
     * Supprimer un employé par son identifiant.
     * 
     * @param id L'identifiant de l'employé à supprimer
     * @throws ResourceNotFoundException si l'employé n'existe pas
     */
    void supprimerParId(Long id);
    
    /**
     * Affecte un employé à une agence.
     * 
     * @param employeId l'identifiant de l'employé
     * @param agenceId l'identifiant de l'agence
     * @throws ResourceNotFoundException si l'employé ou l'agence n'existe pas
     */
    void affecterEmployeAAgence(Long employeId, Long agenceId);
    
    /**
     * Désaffecte un employé d'une agence.
     * 
     * @param employeId l'identifiant de l'employé
     * @throws ResourceNotFoundException si l'employé n'existe pas
     */
    void desaffecterEmployeDeAgence(Long employeId);
}
