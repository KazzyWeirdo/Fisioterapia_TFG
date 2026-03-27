package com.tfg.psychiatrist;

public record PsychiatristPassword(String value) {
        public PsychiatristPassword {
            if (value.isBlank() || value.length() < 12) {
                throw new IllegalArgumentException("Password cannot be smaller than 12 characters");
            }
            if (!value.matches(".*[A-Z].*")) {
                throw new IllegalArgumentException("Password must contain at least one uppercase");
            }
            if (!value.matches(".*[a-z].*")) {
                throw new IllegalArgumentException("Password must contain at least one lowercase");
            }
            if (!value.matches(".*[a-z].*")) {
                throw new IllegalArgumentException("Password must contain at least one lowercase");
            }
            if (!value.matches(".*\\d.*")) {
                throw new IllegalArgumentException("Password must contain at least one digit");
            }
            if (!value.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
                throw new IllegalArgumentException("Password must contain at least one special character");
            }
        }
}