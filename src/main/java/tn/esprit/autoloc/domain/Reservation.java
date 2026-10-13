package tn.esprit.autoloc.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.esprit.autoloc.domain.enums.StatutReservation;

import java.time.LocalDate;

@Entity
@Table(name = "reservation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReservation;

    private LocalDate dateDebut;

    private LocalDate dateFin;

    @Enumerated(EnumType.STRING)
    private StatutReservation statut;

    // Relation N Reservation -> 1 Client
    @ManyToOne
    private Client client;

    // Relation N Reservation -> 1 Vehicule
    @ManyToOne
    private Vehicule vehicule;

    // Relation 1 Reservation -> 1 Contrat (avec cascade ALL)
    @OneToOne(cascade = CascadeType.ALL)
    private Contrat contrat;
}
