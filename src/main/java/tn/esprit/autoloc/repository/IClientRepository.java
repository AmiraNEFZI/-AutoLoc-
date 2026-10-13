package tn.esprit.autoloc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.domain.Client;

/**
 * Repository pour l'entité Client.
 * Étend JpaRepository pour avoir toutes les fonctionnalités.
 */
public interface IClientRepository extends JpaRepository<Client, Long> {
}
