package com.tfg.service.exercisetemplate;

import com.tfg.port.in.exercisetemplate.GetExerciseTemplateByIdUseCase;
import com.tfg.port.out.persistence.ExerciseTemplateRepository;
import com.tfg.trainingsession.ExerciseTemplate;
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
