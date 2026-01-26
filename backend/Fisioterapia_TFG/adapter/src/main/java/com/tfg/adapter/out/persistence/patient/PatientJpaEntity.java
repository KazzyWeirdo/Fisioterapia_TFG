package com.tfg.adapter.out.persistence.patient;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "patients")
public class PatientJpaEntity {
    @Id
    private int id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String surname;
    private String secondSurname;
    @Column(nullable = false)
    private String gender;
    @Column(nullable = false)
    private String dni;
    @Column(nullable = false)
    private String email;
    private int phoneNumber;
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate dateOfBirth;
}
