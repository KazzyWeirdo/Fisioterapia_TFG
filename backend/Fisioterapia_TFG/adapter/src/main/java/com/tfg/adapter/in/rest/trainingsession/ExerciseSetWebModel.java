package com.tfg.adapter.in.rest.trainingsession;

public record ExerciseSetWebModel(
        int setNumber,
        double weightKg,
        int reps,
        int restTimeSeconds,
        int rpe
) {
    static ExerciseSetWebModel fromDomainModel(com.tfg.trainingsession.ExerciseSet exerciseSet) {
        return new ExerciseSetWebModel(
                exerciseSet.setNumber(),
                exerciseSet.weightKg(),
                exerciseSet.reps(),
                exerciseSet.restTimeSeconds(),
                exerciseSet.rpe()
        );
    }
}
