package com.tfg.adapter.out.persistence.patient;

import com.tfg.model.patient.Pathology;
import com.tfg.model.patient.PatientGender;
import com.tfg.model.patient.PatientSex;
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
    private String legalName;
    @Column(nullable = false)
    private String nameToUse;
    @Column(nullable = false)
    private String surname;
    private String secondSurname;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PatientGender genderIdentity;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PatientSex administrativeSex;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PatientSex clinicalUseSex;
    @Column(nullable = false)
    private String dni;
    @Column(nullable = false)
    private String pronouns;
    @Column(nullable = false)
    private String email;
    private int phoneNumber;
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate dateOfBirth;
    private String polarAccessToken;
    private Long polarUserId;
    @Enumerated(EnumType.STRING)
    private Pathology pathology;
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate registrationDate;
    private Integer functionalScore;
    @Temporal(TemporalType.DATE)
    private LocalDate dischargeDate;
}
