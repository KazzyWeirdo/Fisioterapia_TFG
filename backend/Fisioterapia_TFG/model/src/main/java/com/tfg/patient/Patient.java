package com.tfg.patient;

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

    public Patient(String email, String dni, String gender, String sex, String legalName, String nameToUse, String surname, String secondSurname, String pronouns, LocalDate dateOfBirth, int phoneNumber){
        this.id = new PatientId(ThreadLocalRandom.current().nextInt(1_000_000));
        this.email = new PatientEmail(email);
        this.dni = new PatientDNI(dni);
        this.genderIdentity = PatientGender.valueOf(gender);
        this.administrativeSex = PatientSex.valueOf(sex);
        this.legalName = legalName;
        this.nameToUse = nameToUse;
        this.surname = surname;
        this.secondSurname = secondSurname;
        this.pronouns = pronouns;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.polarAccessToken = null;
        this.polarUserId = null;
    }
}
