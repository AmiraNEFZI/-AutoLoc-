package tn.esprit.autoloc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.domain.Paiement;

/**
 * Repository pour l'entité Paiement.
 * Étend JpaRepository pour avoir toutes les fonctionnalités.
 */
public interface IPaiementRepository extends JpaRepository<Paiement, Long> {
}
