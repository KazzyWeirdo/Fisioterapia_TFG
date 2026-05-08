package com.tfg.service.exercisetemplate;

import com.tfg.port.in.exercisetemplate.GetAllExerciseTemplatesUseCase;
import com.tfg.port.out.persistence.ExerciseTemplateRepository;
import com.tfg.trainingsession.ExerciseTemplate;

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
