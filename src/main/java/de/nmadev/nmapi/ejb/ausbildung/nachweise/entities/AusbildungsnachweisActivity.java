package de.nmadev.nmapi.ejb.ausbildung.nachweise.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "ausbildungsnachweis_activity")
@Getter @Setter
public class AusbildungsnachweisActivity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime date;
    private String activity;
    @Column(name = "duration")
    private double durationHours;
}
