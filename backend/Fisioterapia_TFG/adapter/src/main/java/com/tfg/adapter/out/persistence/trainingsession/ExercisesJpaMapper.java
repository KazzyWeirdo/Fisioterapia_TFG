package com.tfg.adapter.out.persistence.trainingsession;

public class ExercisesJpaMapper {
    public static ExercisesJpaEntity toJpaEntity(com.tfg.trainingsession.Exercise exercise, TrainingSessionJpaEntity trainingSessionJpaEntity) {
        ExercisesJpaEntity entity = new ExercisesJpaEntity();
        entity.setId(exercise.getId().value());
        entity.setName(exercise.getName());
        entity.setTrainingSession(trainingSessionJpaEntity);
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
        com.tfg.trainingsession.Exercise model = new com.tfg.trainingsession.Exercise(exercise.getName());
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
