package com.tfg.adapter.in.rest.exercisetemplate;

import com.tfg.adapter.in.rest.trainingsession.ExerciseCreationModel;
import com.tfg.model.trainingsession.ExerciseTemplate;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record ExerciseTemplateCreationModel(
        @NotBlank(message = "Template name is required")
        String name,
        @NotEmpty(message = "At least one exercise is required")
        @Valid
        List<ExerciseCreationModel> exercises
) {
    public ExerciseTemplate toDomainModel() {
        ExerciseTemplate template = new ExerciseTemplate(name);
        exercises.forEach(exerciseCreationModel -> template.addExercise(exerciseCreationModel.toDomainModel()));
        return template;
    }
}
