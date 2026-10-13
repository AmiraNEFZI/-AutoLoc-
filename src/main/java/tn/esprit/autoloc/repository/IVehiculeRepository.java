package tn.esprit.autoloc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.domain.Vehicule;

/**
 * Repository pour l'entité Vehicule.
 * Étend JpaRepository pour avoir toutes les fonctionnalités.
 */
public interface IVehiculeRepository extends JpaRepository<Vehicule, Long> {
}
