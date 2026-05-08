package com.tfg.port.in.exercisetemplate;

import com.tfg.trainingsession.ExerciseTemplate;
import java.util.Optional;

public interface GetExerciseTemplateByIdUseCase {
    Optional<ExerciseTemplate> getById(int id);
}
