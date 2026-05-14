package com.tfg.patient;

public record PatientEmail(String value) {
    public PatientEmail {
        if (value == null || !value.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("'value' must be a valid email address");
        }
    }
}
