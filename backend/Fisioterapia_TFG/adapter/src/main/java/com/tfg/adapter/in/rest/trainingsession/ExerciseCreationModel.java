package com.tfg.adapter.in.rest.trainingsession;

import com.tfg.trainingsession.Exercise;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ExerciseCreationModel(
        @NotNull(message = "Name for the exercise is required")
        String name,
        @Valid
        List<ExerciseSetCreationModel> exercises
) {
        public Exercise toDomainModel() {
                Exercise exercise = new Exercise(name);
                exercises.forEach(exerciseSetCreationModel -> exercise.addSet(exerciseSetCreationModel.toDomain()));
                return exercise;
        }
}
