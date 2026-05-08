package com.tfg.port.out.persistence;

import com.tfg.trainingsession.ExerciseTemplate;

import java.util.List;
import java.util.Optional;

public interface ExerciseTemplateRepository {
    void save(ExerciseTemplate template);
    List<ExerciseTemplate> findAll();
    Optional<ExerciseTemplate> findById(int id);
    void deleteAll();
}
