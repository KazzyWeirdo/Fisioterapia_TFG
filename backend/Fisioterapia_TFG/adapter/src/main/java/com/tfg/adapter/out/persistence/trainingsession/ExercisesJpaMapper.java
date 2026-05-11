package com.tfg.adapter.out.persistence.trainingsession;

import com.tfg.adapter.out.persistence.exercisetemplate.ExerciseTemplateJpaEntity;
import com.tfg.model.trainingsession.Exercise;
import com.tfg.model.trainingsession.ExerciseId;
import com.tfg.model.trainingsession.ExerciseSet;

public class ExercisesJpaMapper {
    public static ExercisesJpaEntity toJpaEntity(Exercise exercise, ExerciseTemplateJpaEntity exerciseTemplateJpaEntity) {
        ExercisesJpaEntity entity = new ExercisesJpaEntity();
        entity.setId(exercise.getId().value());
        entity.setName(exercise.getName());
        entity.setExerciseTemplate(exerciseTemplateJpaEntity);
        entity.setSets(exercise.getSets().stream().map(set -> {
            ExerciseSetJpaEmbeddable embeddable = new ExerciseSetJpaEmbeddable();
            embeddable.setRestTimeSeconds(set.restTimeSeconds());
            embeddable.setReps(set.reps());
            embeddable.setRpe(set.rpe());
            embeddable.setSetNumber(set.setNumber());
            embeddable.setWeightKg(set.weightKg());
            return embeddable;
        }).toList());
        return entity;
    }

    public static Exercise toModelEntity(ExercisesJpaEntity exercise) {
        Exercise model = new Exercise(
                new ExerciseId(exercise.getId()),
                exercise.getName(),
                new java.util.ArrayList<>()
        );
        exercise.getSets().forEach(set -> model.addSet(new ExerciseSet(
                set.getSetNumber(),
                set.getWeightKg(),
                set.getReps(),
                set.getRestTimeSeconds(),
                set.getRpe()
        )));
        return model;
    }
}
