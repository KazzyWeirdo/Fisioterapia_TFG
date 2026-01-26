package com.tfg.patient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

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
}
