package tn.esprit.autoloc.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IContratRepository;
import tn.esprit.autoloc.service.IContratService;
import tn.esprit.autoloc.web.dto.ContratDTO;
import tn.esprit.autoloc.web.mapper.ContratMapper;

/**
 * Implémentation du service Contrat utilisant des DTO.
 * 
 * Partie 3.4 du TP : Le service injecte le repository et le mapper correspondant.
 * Pattern : convertir DTO en entité, enregistrer l'entité, convertir résultat en DTO.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ContratServiceImpl implements IContratService {

    private final IContratRepository contratRepository;
    private final ContratMapper contratMapper;

    @Override
    public ContratDTO creer(ContratDTO dto) {
        // Convertir DTO en entité
        Contrat contrat = contratMapper.toEntity(dto);
        
        // Validation métier
        if (contrat.getIdContrat() != null) {
            throw new IllegalArgumentException(
                    "Un nouveau contrat ne doit pas avoir d'identifiant");
        }
        
        // Enregistrer l'entité
        Contrat saved = contratRepository.save(contrat);
        
        // Convertir résultat en DTO
        return contratMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ContratDTO obtenirParId(Long id) {
        Contrat contrat = contratRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contrat", id));
        return contratMapper.toDto(contrat);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContratDTO> listerTout() {
        return contratRepository.findAll().stream()
                .map(contratMapper::toDto)
                .toList();
    }

    @Override
    public ContratDTO modifier(Long id, ContratDTO dto) {
        // Rechercher l'entité existante
        Contrat existant = contratRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contrat", id));
        
        // Mettre à jour les attributs
        existant.setMontantTotal(dto.getMontantTotal());
        existant.setValide(dto.getValide());
        existant.setDateSignature(dto.getDateSignature());
        
        // Enregistrer et convertir en DTO
        Contrat saved = contratRepository.save(existant);
        return contratMapper.toDto(saved);
    }

    @Override
    public void supprimerParId(Long id) {
        if (!contratRepository.existsById(id)) {
            throw new ResourceNotFoundException("Contrat", id);
        }
        contratRepository.deleteById(id);
    }
}

