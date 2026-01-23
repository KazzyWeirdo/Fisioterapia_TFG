package com.tfg.patient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

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
    private Date dateOfBirth;
    private int phoneNumber;
}
