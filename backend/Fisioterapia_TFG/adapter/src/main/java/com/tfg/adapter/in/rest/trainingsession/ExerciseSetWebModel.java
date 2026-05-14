package com.tfg.adapter.in.rest.trainingsession;

import com.tfg.model.trainingsession.ExerciseSet;

public record ExerciseSetWebModel(
        int setNumber,
        double weightKg,
        int reps,
        int restTimeSeconds,
        int rpe
) {
    static ExerciseSetWebModel fromDomainModel(ExerciseSet exerciseSet) {
        return new ExerciseSetWebModel(
                exerciseSet.setNumber(),
                exerciseSet.weightKg(),
                exerciseSet.reps(),
                exerciseSet.restTimeSeconds(),
                exerciseSet.rpe()
        );
    }
}
