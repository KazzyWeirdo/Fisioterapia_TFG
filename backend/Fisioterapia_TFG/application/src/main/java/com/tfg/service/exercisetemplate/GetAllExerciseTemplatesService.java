package com.tfg.service.exercisetemplate;

import com.tfg.port.in.exercisetemplate.GetAllExerciseTemplatesUseCase;
import com.tfg.application.port.out.persistence.ExerciseTemplateRepository;
import com.tfg.model.trainingsession.ExerciseTemplate;

import java.util.List;

public class GetAllExerciseTemplatesService implements GetAllExerciseTemplatesUseCase {

    private final ExerciseTemplateRepository exerciseTemplateRepository;

    public GetAllExerciseTemplatesService(ExerciseTemplateRepository exerciseTemplateRepository) {
        this.exerciseTemplateRepository = exerciseTemplateRepository;
    }

    @Override
    public List<ExerciseTemplate> getAllExerciseTemplates() {
        return exerciseTemplateRepository.findAll();
    }
}
