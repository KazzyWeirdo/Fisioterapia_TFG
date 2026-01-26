package com.tfg.patient;

/**
 * A patient ID value object (enabling type-safety and validation).
 *
 * @author Sven Woltmann
 */

public record PatientId(int value) {
    public PatientId {
        if (value < 1) {
            throw new IllegalArgumentException("'value' must be a positive integer");
        }
    }
}
