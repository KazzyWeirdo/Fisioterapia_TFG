package com.tfg.trainingsession;

public record ExerciseSet(
        int setNumber,
        double weightKg,
        int reps,
        int restTimeSeconds,
        int rpe
) {
    public ExerciseSet {
        if (setNumber <= 0) throw new IllegalArgumentException("Set number must be greater than 0");
        if (weightKg < 0) throw new IllegalArgumentException("Weight cannot be negative");
        if (reps < 0) throw new IllegalArgumentException("Repetitions cannnot be negative");
        if (restTimeSeconds < 0) throw new IllegalArgumentException("Rest time cannot be negative");
        if (rpe < 1 || rpe > 10) throw new IllegalArgumentException("RPE must be a value between 0 and 10");
    }
}
