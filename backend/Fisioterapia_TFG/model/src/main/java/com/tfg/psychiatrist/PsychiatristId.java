package com.tfg.psychiatrist;

public record PsychiatristId(int value) {
        public PsychiatristId {
            if (value <= 0) {
                throw new IllegalArgumentException("Psychiatrist ID must be a positive integer.");
            }
        }
}
