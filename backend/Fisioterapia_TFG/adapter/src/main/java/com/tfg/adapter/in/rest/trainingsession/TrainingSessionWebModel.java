package com.tfg.adapter.in.rest.trainingsession;

import java.time.LocalDate;
import java.util.List;

public record TrainingSessionWebModel(
        int id,
        int patientId,
        LocalDate date,
        List<ExerciseWebModel> exercises
) {
    static TrainingSessionWebModel fromDomainModel(com.tfg.trainingsession.TrainingSession trainingSession) {
        List<ExerciseWebModel> exerciseWebModels = trainingSession.getExercises().stream()
                .map(ExerciseWebModel::fromDomainModel)
                .toList();

        return new TrainingSessionWebModel(
                trainingSession.getId().value(),
                trainingSession.getPatient().getId().value(),
                trainingSession.getDate(),
                exerciseWebModels
        );
    }
}
