package com.tfg.model.patient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

@AllArgsConstructor
@Setter
@Getter
public class Patient {
    private final PatientId id;
    private final PatientEmail email;
    private final PatientDNI dni;
    private PatientGender genderIdentity;
    private PatientSex clinicalUseSex;
    private PatientSex administrativeSex;
    private String legalName;
    private String nameToUse;
    private String surname;
    private String secondSurname;
    private String pronouns;
    private LocalDate dateOfBirth;
    private int phoneNumber;
    private String polarAccessToken;
    private Long polarUserId;
    private Pathology pathology;
    private LocalDate registrationDate;
    private Integer functionalScore;
    private LocalDate dischargeDate;

    public Patient(String email, String dni, String gender, String clinicalSex, String administrativeSex, String legalName, String nameToUse, String surname, String secondSurname, String pronouns, LocalDate dateOfBirth, int phoneNumber, String pathology){
        this.id = new PatientId(ThreadLocalRandom.current().nextInt(1_000_000));
        this.email = new PatientEmail(email);
        this.dni = new PatientDNI(dni);
        this.genderIdentity = PatientGender.valueOf(gender);
        this.clinicalUseSex = PatientSex.valueOf(clinicalSex);
        this.administrativeSex = PatientSex.valueOf(administrativeSex);
        this.legalName = legalName;
        this.nameToUse = nameToUse;
        this.surname = surname;
        this.secondSurname = secondSurname;
        this.pronouns = pronouns;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.polarAccessToken = null;
        this.polarUserId = null;
        this.pathology = Pathology.valueOf(pathology);
        this.registrationDate = LocalDate.now();
        this.functionalScore = null;
        this.dischargeDate = null;
    }

    public void applyFunctionalScore(int score) {
        if (score < 0 || score > 100) throw new IllegalArgumentException("Functional score must be between 0 and 100");
        this.functionalScore = score;
    }

    public void discharge(LocalDate today) {
        if (this.dischargeDate != null) return;
        this.dischargeDate = today;
    }
}
