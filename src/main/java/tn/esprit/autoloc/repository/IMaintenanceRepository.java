package tn.esprit.autoloc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.domain.Maintenance;

/**
 * Repository pour l'entité Maintenance.
 * Étend JpaRepository pour avoir toutes les fonctionnalités.
 */
public interface IMaintenanceRepository extends JpaRepository<Maintenance, Long> {
}
