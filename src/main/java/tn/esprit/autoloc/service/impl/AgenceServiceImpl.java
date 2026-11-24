package tn.esprit.autoloc.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IAgenceRepository;
import tn.esprit.autoloc.service.IAgenceService;
import tn.esprit.autoloc.web.dto.AgenceDTO;
import tn.esprit.autoloc.web.mapper.AgenceMapper;

/**
 * Implémentation du service Agence utilisant des DTO.
 * 
 * Partie 3 du TP : Le service injecte le repository et le mapper correspondant.
 * Lors de la création : convertir DTO en entité, enregistrer l'entité, convertir résultat en DTO.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class AgenceServiceImpl implements IAgenceService {

    private final IAgenceRepository agenceRepository;
    private final AgenceMapper agenceMapper;

    @Override
    public AgenceDTO creer(AgenceDTO dto) {
        // Convertir DTO en entité
        Agence agence = agenceMapper.toEntity(dto);
        
        // Validation métier
        if (agence.getIdAgence() != null) {
            throw new IllegalArgumentException(
                    "Une nouvelle agence ne doit pas avoir d'identifiant");
        }
        if (agence.getNom() == null || agence.getNom().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom de l'agence est obligatoire");
        }
        
        // Enregistrer l'entité
        Agence saved = agenceRepository.save(agence);
        
        // Convertir résultat en DTO
        return agenceMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public AgenceDTO obtenirParId(Long id) {
        Agence agence = agenceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agence", id));
        return agenceMapper.toDto(agence);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgenceDTO> listerTout() {
        return agenceRepository.findAll().stream()
                .map(agenceMapper::toDto)
                .toList();
    }

    @Override
    public AgenceDTO modifier(Long id, AgenceDTO dto) {
        // Rechercher l'entité existante
        Agence existant = agenceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agence", id));
        
        // Mettre à jour les attributs
        existant.setNom(dto.getNom());
        existant.setAdresse(dto.getAdresse());
        existant.setTelephone(dto.getTelephone());
        existant.setVille(dto.getVille());
        
        // Enregistrer et convertir en DTO
        Agence saved = agenceRepository.save(existant);
        return agenceMapper.toDto(saved);
    }

    @Override
    public void supprimerParId(Long id) {
        if (!agenceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Agence", id);
        }
        agenceRepository.deleteById(id);
    }
}
