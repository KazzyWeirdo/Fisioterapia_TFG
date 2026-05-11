package com.tfg.application.port.in.exercisetemplate;

import com.tfg.model.trainingsession.ExerciseTemplate;
import java.util.Optional;

public interface GetExerciseTemplateByIdUseCase {
    Optional<ExerciseTemplate> getById(int id);
}
