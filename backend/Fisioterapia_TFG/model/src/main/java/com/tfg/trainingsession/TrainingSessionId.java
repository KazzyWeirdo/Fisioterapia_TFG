package com.tfg.trainingsession;

public record TrainingSessionId(int value) {
    public TrainingSessionId {
        if (value < 1) {
            throw new IllegalArgumentException("'id' must be a positive integer");
        }
    }
}
