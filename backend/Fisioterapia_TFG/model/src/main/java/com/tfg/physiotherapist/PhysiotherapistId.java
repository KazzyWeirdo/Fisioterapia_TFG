package com.tfg.physiotherapist;

public record PhysiotherapistId(int value) {
        public PhysiotherapistId {
            if (value <= 0) {
                throw new IllegalArgumentException("Psychiatrist ID must be a positive integer.");
            }
        }
}
