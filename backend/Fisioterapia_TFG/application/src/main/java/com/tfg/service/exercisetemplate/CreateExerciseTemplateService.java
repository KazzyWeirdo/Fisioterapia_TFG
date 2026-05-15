package com.tfg.service.exercisetemplate;

import com.tfg.port.in.exercisetemplate.CreateExerciseTemplateUseCase;
import com.tfg.application.port.out.persistence.ExerciseTemplateRepository;
import com.tfg.model.trainingsession.ExerciseTemplate;

public class CreateExerciseTemplateService implements CreateExerciseTemplateUseCase {

    private final ExerciseTemplateRepository exerciseTemplateRepository;

    public CreateExerciseTemplateService(ExerciseTemplateRepository exerciseTemplateRepository) {
        this.exerciseTemplateRepository = exerciseTemplateRepository;
    }

    @Override
    public void createExerciseTemplate(ExerciseTemplate template) {
        exerciseTemplateRepository.save(template);
    }
}
