package com.tfg.adapter.in.rest.trainingsession;

import java.util.List;

public record ExerciseWebModel(
        int id,
        String name,
        List<ExerciseSetWebModel> sets
) {
        static ExerciseWebModel fromDomainModel(com.tfg.trainingsession.Exercise exercise) {
            List<ExerciseSetWebModel> exerciseSetWebModels = exercise.getSets().stream()
                    .map(ExerciseSetWebModel::fromDomainModel)
                    .toList();

            return new ExerciseWebModel(
                    exercise.getId().value(),
                    exercise.getName(),
                    exerciseSetWebModels
            );
        }
}
