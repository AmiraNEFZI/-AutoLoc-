package tn.esprit.autoloc.service.impl;

import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IAgenceRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;
import tn.esprit.autoloc.service.IVehiculeService;
import tn.esprit.autoloc.web.dto.VehiculeDTO;
import tn.esprit.autoloc.web.mapper.VehiculeMapper;

/**
 * Implémentation du service Vehicule utilisant des DTO.
 * 
 * Partie 3.4 du TP : Le service injecte le repository et le mapper correspondant.
 * Pattern : convertir DTO en entité, enregistrer l'entité, convertir résultat en DTO.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class VehiculeServiceImpl implements IVehiculeService {

    private final IVehiculeRepository vehiculeRepository;
    private final IAgenceRepository agenceRepository;
    private final VehiculeMapper vehiculeMapper;

    @Override
    public VehiculeDTO creer(VehiculeDTO dto) {
        // Convertir DTO en entité
        Vehicule vehicule = vehiculeMapper.toEntity(dto);
        
        // Validation métier
        if (vehicule.getIdVehicule() != null) {
            throw new IllegalArgumentException(
                    "Un nouveau véhicule ne doit pas avoir d'identifiant");
        }
        if (vehicule.getTarifJournalier() != null && 
            vehicule.getTarifJournalier().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Le tarif journalier ne peut pas être négatif");
        }
        
        // Enregistrer l'entité
        Vehicule saved = vehiculeRepository.save(vehicule);
        
        // Convertir résultat en DTO
        return vehiculeMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public VehiculeDTO obtenirParId(Long id) {
        Vehicule vehicule = vehiculeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicule", id));
        return vehiculeMapper.toDto(vehicule);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehiculeDTO> listerTout() {
        return vehiculeRepository.findAll().stream()
                .map(vehiculeMapper::toDto)
                .toList();
    }

    @Override
    public VehiculeDTO modifier(Long id, VehiculeDTO dto) {
        // Rechercher l'entité existante
        Vehicule existant = vehiculeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicule", id));
        
        // Mettre à jour les attributs
        existant.setImmatriculation(dto.getImmatriculation());
        existant.setMarque(dto.getMarque());
        existant.setModele(dto.getModele());
        existant.setCategorie(dto.getCategorie());
        existant.setTarifJournalier(dto.getTarifJournalier());
        existant.setStatut(dto.getStatut());
        
        // Enregistrer et convertir en DTO
        Vehicule saved = vehiculeRepository.save(existant);
        return vehiculeMapper.toDto(saved);
    }

    @Override
    public void supprimerParId(Long id) {
        if (!vehiculeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Vehicule", id);
        }
        vehiculeRepository.deleteById(id);
    }
    
    /**
     * Affecte un véhicule à une agence.
     * Cette méthode suit la logique du TP :
     * 1. Rechercher le véhicule et l'agence
     * 2. Si l'un des deux n'existe pas, retourner une erreur métier explicite
     * 3. Mettre à jour l'association du côté propriétaire (Vehicule)
     * 4. Enregistrer l'entité avec le repository approprié
     */
    @Override
    public void affecterVehiculeAAgence(Long vehiculeId, Long agenceId) {
        // Rechercher le véhicule
        Vehicule vehicule = vehiculeRepository.findById(vehiculeId)
                .orElseThrow(() -> new ResourceNotFoundException("Véhicule introuvable", vehiculeId));
        
        // Rechercher l'agence
        Agence agence = agenceRepository.findById(agenceId)
                .orElseThrow(() -> new ResourceNotFoundException("Agence introuvable", agenceId));
        
        // Mettre à jour l'association du côté propriétaire
        vehicule.setAgence(agence);
        
        // Enregistrer le véhicule
        vehiculeRepository.save(vehicule);
    }
    
    /**
     * Désaffecte un véhicule d'une agence.
     * Cette méthode suit la logique du TP :
     * 1. Vérifier que le véhicule existe et qu'il est actuellement rattaché à une agence
     * 2. Mettre à null la relation du côté propriétaire
     * 3. Enregistrer le véhicule
     * 4. Définir le comportement attendu si le véhicule n'est rattaché à aucune agence
     */
    @Override
    public void desaffecterVehiculDeAgence(Long vehiculeId) {
        // Rechercher le véhicule
        Vehicule vehicule = vehiculeRepository.findById(vehiculeId)
                .orElseThrow(() -> new ResourceNotFoundException("Véhicule introuvable", vehiculeId));
        
        // Mettre à null la relation du côté propriétaire
        vehicule.setAgence(null);
        
        // Enregistrer le véhicule
        vehiculeRepository.save(vehicule);
    }
}
