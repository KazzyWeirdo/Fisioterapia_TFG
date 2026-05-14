package com.tfg.application.port.out.persistence;

import com.tfg.model.trainingsession.ExerciseTemplate;

import java.util.List;
import java.util.Optional;

public interface ExerciseTemplateRepository {
    void save(ExerciseTemplate template);
    List<ExerciseTemplate> findAll();
    Optional<ExerciseTemplate> findById(int id);
    void deleteAll();
}
