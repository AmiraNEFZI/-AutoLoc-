package tn.esprit.autoloc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.domain.Employe;

/**
 * Repository pour l'entité Employe.
 * Étend JpaRepository pour avoir toutes les fonctionnalités.
 */
public interface IEmployeRepository extends JpaRepository<Employe, Long> {
}
