package tn.esprit.autoloc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.domain.Agence;

/**
 * Repository pour l'entité Agence.
 * Étend JpaRepository pour avoir toutes les fonctionnalités :
 * - CRUD (save, findById, delete...)
 * - Listes (findAll retourne List)
 * - Tri et pagination
 * - Méthodes JPA (flush, saveAndFlush...)
 */
public interface IAgenceRepository extends JpaRepository<Agence, Long> {
}
