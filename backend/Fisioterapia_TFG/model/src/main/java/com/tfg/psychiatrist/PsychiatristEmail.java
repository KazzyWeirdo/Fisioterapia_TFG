package com.tfg.psychiatrist;

public record PsychiatristEmail(String value) {
     public PsychiatristEmail {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank");
        }
        if (!value.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }
}
