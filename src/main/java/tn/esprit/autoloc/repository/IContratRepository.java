package tn.esprit.autoloc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.domain.Contrat;

/**
 * Repository pour l'entité Contrat.
 * Étend JpaRepository pour avoir toutes les fonctionnalités :
 * - CRUD (save, findById, delete...)
 * - Listes (findAll retourne List)
 * - Tri et pagination
 * - Méthodes JPA (flush, saveAndFlush...)
 * 
 * Ce repository sera utilisé pour observer les cascades (CascadeType.ALL)
 * et l'orphanRemoval sur la relation Contrat -> Paiement.
 */
public interface IContratRepository extends JpaRepository<Contrat, Long> {
}
