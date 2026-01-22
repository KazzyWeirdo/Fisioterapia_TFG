package com.tfg.patient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
@AllArgsConstructor
public class Patient {
    private PatientId id;
    private String name;
    private String surname;
    private String secondSurname;
    private String email;
    private int phoneNumber;
}
