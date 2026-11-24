package tn.esprit.autoloc.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IAgenceRepository;
import tn.esprit.autoloc.repository.IEmployeRepository;
import tn.esprit.autoloc.service.IEmployeService;
import tn.esprit.autoloc.web.dto.EmployeDTO;
import tn.esprit.autoloc.web.mapper.EmployeMapper;

/**
 * Implémentation du service Employe utilisant des DTO.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class EmployeServiceImpl implements IEmployeService {

    private final IEmployeRepository employeRepository;
    private final IAgenceRepository agenceRepository;
    private final EmployeMapper employeMapper;

    @Override
    public EmployeDTO creer(EmployeDTO dto) {
        // Convertir DTO en entité
        Employe employe = employeMapper.toEntity(dto);
        
        // Validation métier
        if (employe.getIdEmploye() != null) {
            throw new IllegalArgumentException(
                    "Un nouvel employé ne doit pas avoir d'identifiant");
        }
        if (employe.getNom() == null || employe.getNom().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom de l'employé est obligatoire");
        }
        if (employe.getRole() == null) {
            throw new IllegalArgumentException("Le poste de l'employé est obligatoire");
        }
        
        // Enregistrer l'entité
        Employe saved = employeRepository.save(employe);
        
        // Convertir résultat en DTO
        return employeMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeDTO obtenirParId(Long id) {
        Employe employe = employeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employe", id));
        return employeMapper.toDto(employe);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeDTO> listerTout() {
        return employeRepository.findAll().stream()
                .map(employeMapper::toDto)
                .toList();
    }

    @Override
    public EmployeDTO modifier(Long id, EmployeDTO dto) {
        // Rechercher l'entité existante
        Employe existant = employeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employe", id));
        
        // Mettre à jour les attributs
        existant.setNom(dto.getNom());
        existant.setPrenom(dto.getPrenom());
        existant.setRole(dto.getRole());
        
        // Enregistrer et convertir en DTO
        Employe saved = employeRepository.save(existant);
        return employeMapper.toDto(saved);
    }

    @Override
    public void supprimerParId(Long id) {
        if (!employeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Employe", id);
        }
        employeRepository.deleteById(id);
    }
    
    /**
     * Affecte un employé à une agence.
     * Cette méthode suit la logique du TP :
     * 1. Rechercher l'employé et l'agence
     * 2. Si l'un des deux n'existe pas, retourner une erreur métier explicite
     * 3. Mettre à jour l'association du côté propriétaire (Employe)
     * 4. Enregistrer l'entité concernée
     */
    @Override
    public void affecterEmployeAAgence(Long employeId, Long agenceId) {
        // Rechercher l'employé
        Employe employe = employeRepository.findById(employeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employé introuvable", employeId));
        
        // Rechercher l'agence
        Agence agence = agenceRepository.findById(agenceId)
                .orElseThrow(() -> new ResourceNotFoundException("Agence introuvable", agenceId));
        
        // Mettre à jour l'association du côté propriétaire de l'association
        employe.setAgence(agence);
        
        // Sauvegarder l'entité concernée
        employeRepository.save(employe);
    }
    
    /**
     * Désaffecte un employé d'une agence.
     * Cette méthode suit la logique du TP :
     * 1. Vérifier que l'employé existe
     * 2. Mettre à null la relation du côté propriétaire
     * 3. Enregistrer l'employé
     */
    @Override
    public void desaffecterEmployeDeAgence(Long employeId) {
        // Rechercher l'employé
        Employe employe = employeRepository.findById(employeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employé introuvable", employeId));
        
        // Mettre à null la relation du côté propriétaire
        employe.setAgence(null);
        
        // Enregistrer l'employé
        employeRepository.save(employe);
    }
}
