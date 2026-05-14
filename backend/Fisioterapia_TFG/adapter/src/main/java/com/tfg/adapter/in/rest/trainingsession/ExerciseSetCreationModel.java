package com.tfg.adapter.in.rest.trainingsession;

import com.tfg.trainingsession.ExerciseSet;
import jakarta.validation.constraints.NotNull;

public record ExerciseSetCreationModel(
        @NotNull(message = "Set number is required")
        int setNumber,
        @NotNull(message = "Weight in kg is required")
        double weightKg,
        @NotNull(message = "Number of repetitions is required")
        int reps,
        @NotNull(message = "Rest time in seconds is required")
        int restTimeSeconds,
        @NotNull(message = "RPE is required")
        int rpe
) {
    public ExerciseSet toDomain() {
        return new ExerciseSet(setNumber, weightKg, reps, restTimeSeconds, rpe);
    }
}
