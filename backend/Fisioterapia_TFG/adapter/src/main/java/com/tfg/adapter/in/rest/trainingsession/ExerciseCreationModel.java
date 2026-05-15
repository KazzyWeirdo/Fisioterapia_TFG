package com.tfg.adapter.in.rest.trainingsession;

import com.tfg.model.trainingsession.Exercise;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ExerciseCreationModel(
        @NotNull(message = "Name for the exercise is required")
        String name,
        @Valid
        List<ExerciseSetCreationModel> sets
) {
        public Exercise toDomainModel() {
                Exercise exercise = new Exercise(name.trim().toLowerCase());
                if (sets != null) {
                        sets.forEach(s -> exercise.addSet(s.toDomain()));
                }
                return exercise;
        }
}
