package tn.esprit.autoloc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.domain.Equipement;

/**
 * Repository pour l'entité Equipement.
 * Étend JpaRepository pour avoir toutes les fonctionnalités.
 */
public interface IEquipementRepository extends JpaRepository<Equipement, Long> {
}
