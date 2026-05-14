package com.tfg.adapter.out.persistence.exercisetemplate;

import com.tfg.adapter.out.persistence.trainingsession.ExercisesJpaEntity;
import com.tfg.adapter.out.persistence.trainingsession.ExercisesJpaMapper;
import com.tfg.trainingsession.ExerciseTemplate;
import com.tfg.trainingsession.ExerciseTemplateId;

import java.util.ArrayList;

public class ExerciseTemplateJpaMapper {
    public static ExerciseTemplateJpaEntity toJpaEntity(ExerciseTemplate template) {
        ExerciseTemplateJpaEntity entity = new ExerciseTemplateJpaEntity();
        entity.setId(template.getId().value());
        entity.setName(template.getName());
        template.getExercises().forEach(exercise -> {
            ExercisesJpaEntity exerciseJpaEntity = ExercisesJpaMapper.toJpaEntity(exercise, entity);
            entity.addExercise(exerciseJpaEntity);
        });
        return entity;
    }

    public static ExerciseTemplate toModel(ExerciseTemplateJpaEntity entity) {
        ExerciseTemplate template = new ExerciseTemplate(
                new ExerciseTemplateId(entity.getId()),
                entity.getName(),
                new ArrayList<>()
        );
        entity.getExercises().forEach(exerciseJpa ->
                template.addExercise(ExercisesJpaMapper.toModelEntity(exerciseJpa)));
        return template;
    }
}
