package com.tfg.service.exercisetemplate;

import com.tfg.port.in.exercisetemplate.GetExerciseTemplateByIdUseCase;
import com.tfg.application.port.out.persistence.ExerciseTemplateRepository;
import com.tfg.model.trainingsession.ExerciseTemplate;
import java.util.Optional;

public class GetExerciseTemplateByIdService implements GetExerciseTemplateByIdUseCase {
    private final ExerciseTemplateRepository exerciseTemplateRepository;

    public GetExerciseTemplateByIdService(ExerciseTemplateRepository exerciseTemplateRepository) {
        this.exerciseTemplateRepository = exerciseTemplateRepository;
    }

    @Override
    public Optional<ExerciseTemplate> getById(int id) {
        return exerciseTemplateRepository.findById(id);
    }
}
