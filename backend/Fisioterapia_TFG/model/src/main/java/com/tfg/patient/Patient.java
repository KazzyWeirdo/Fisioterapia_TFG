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
    private PatientGender gender;
    private String name;
    private String surname;
    private String secondSurname;
    private LocalDate dateOfBirth;
    private int phoneNumber;

    public Patient(String email, String dni, String gender, String name, String surname, String secondSurname, LocalDate dateOfBirth, int phoneNumber){
        this.id = new PatientId(ThreadLocalRandom.current().nextInt(1_000_000));
        this.email = new PatientEmail(email);
        this.dni = new PatientDNI(dni);
        this.gender = PatientGender.valueOf(gender);
        this.name = name;
        this.surname = surname;
        this.secondSurname = secondSurname;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
    }
}
