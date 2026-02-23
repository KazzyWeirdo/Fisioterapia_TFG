package com.tfg.trainingsession;

public record ExerciseId(int value) {
    public ExerciseId {
        if (value < 1) {
            throw new IllegalArgumentException("'id' must be a positive integer");
        }
    }
}
