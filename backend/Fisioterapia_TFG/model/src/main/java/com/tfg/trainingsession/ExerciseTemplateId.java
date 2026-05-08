package com.tfg.trainingsession;

public record ExerciseTemplateId(int value) {
    public ExerciseTemplateId {
        if (value < 1) {
            throw new IllegalArgumentException("'id' must be a positive integer");
        }
    }
}
