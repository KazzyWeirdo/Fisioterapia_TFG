package com.tfg.adapter.in.rest.trainingsession;

import com.tfg.model.trainingsession.Exercise;

import java.util.List;

public record ExerciseWebModel(
        int id,
        String name,
        List<ExerciseSetWebModel> sets
) {
        static ExerciseWebModel fromDomainModel(Exercise exercise) {
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
