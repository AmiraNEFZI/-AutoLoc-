package tn.esprit.autoloc.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.esprit.autoloc.domain.enums.CategorieVehicule;
import tn.esprit.autoloc.domain.enums.StatutVehicule;

import java.math.BigDecimal;
import java.util.*;

@Entity
@Table(name = "vehicule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Vehicule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVehicule;

    private String immatriculation;

    private String marque;

    private String modele;

    @Enumerated(EnumType.STRING)
    private CategorieVehicule categorie;

    private BigDecimal tarifJournalier;

    @Enumerated(EnumType.STRING)
    private StatutVehicule statut;

    // Relation N Vehicule -> 1 Agence
    @ManyToOne
    private Agence agence;

    // Relation 1 Vehicule -> N Reservation
    @OneToMany(mappedBy = "vehicule")
    private List<Reservation> reservations;

    // Relation 1 Vehicule -> N Maintenance
    @OneToMany(mappedBy = "vehicule")
    private List<Maintenance> maintenances;

    // Relation N Vehicule <-> N Equipement (ManyToMany)
    @ManyToMany
    @JoinTable(
        name = "vehicule_equipements",
        joinColumns = @JoinColumn(name = "vehicule_id"),
        inverseJoinColumns = @JoinColumn(name = "equipement_id")
    )
    private List<Equipement> equipements;
}
