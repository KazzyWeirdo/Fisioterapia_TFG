package com.tfg.adapter.out.persistence.trainingsession;

import com.tfg.adapter.out.persistence.exercisetemplate.ExerciseTemplateJpaEntity;

public class ExercisesJpaMapper {
    public static ExercisesJpaEntity toJpaEntity(com.tfg.trainingsession.Exercise exercise, ExerciseTemplateJpaEntity exerciseTemplateJpaEntity) {
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

    public static com.tfg.trainingsession.Exercise toModelEntity(ExercisesJpaEntity exercise) {
        com.tfg.trainingsession.Exercise model = new com.tfg.trainingsession.Exercise(
                new com.tfg.trainingsession.ExerciseId(exercise.getId()),
                exercise.getName(),
                new java.util.ArrayList<>()
        );
        exercise.getSets().forEach(set -> model.addSet(new com.tfg.trainingsession.ExerciseSet(
                set.getSetNumber(),
                set.getWeightKg(),
                set.getReps(),
                set.getRestTimeSeconds(),
                set.getRpe()
        )));
        return model;
    }
}
