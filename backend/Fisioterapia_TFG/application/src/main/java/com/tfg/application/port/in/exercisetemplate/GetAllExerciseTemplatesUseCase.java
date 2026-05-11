package com.tfg.application.port.in.exercisetemplate;

import com.tfg.model.trainingsession.ExerciseTemplate;

import java.util.List;

public interface GetAllExerciseTemplatesUseCase {
    List<ExerciseTemplate> getAllExerciseTemplates();
}
