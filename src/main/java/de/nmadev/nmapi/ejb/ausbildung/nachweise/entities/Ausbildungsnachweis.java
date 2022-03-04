package de.nmadev.nmapi.ejb.ausbildung.nachweise.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "ausbildungsnachweis")
@Getter @Setter
public class Ausbildungsnachweis implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int ausbildungsjahr;
    private String abteilung;
    private boolean daily;

    @Column(name = "start_date")
    private LocalDateTime startDate;
    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "azubi_id")
    private Long azubiId;
    @Column(name = "signed_azubi")
    private boolean signedByAzubi;
    @Column(name = "ausbilder_id")
    private Long ausbilderId;
    @Column(name = "signed_ausbilder")
    private boolean signedByAusbilder;
}
