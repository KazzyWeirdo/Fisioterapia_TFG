package com.tfg.patient;

public record PatientDNI(String value) {
    public PatientDNI {
        if (value == null || !value.matches("^[0-9]{8}[A-Za-z]$")) {
            throw new IllegalArgumentException("'value' must be a valid DNI format (8 digits followed by a letter)");
        }
    }
}
