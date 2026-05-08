package com.tfg.adapter.in.rest.trainingsession;

import java.time.LocalDateTime;
import java.util.List;

public record TrainingSessionWebModel(
        int id,
        int patientId,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        String physiotherapistName,
        String templateName,
        List<ExerciseWebModel> exercises
) {
    static TrainingSessionWebModel fromDomainModel(com.tfg.trainingsession.TrainingSession trainingSession) {
        String physioName = trainingSession.getPhysiotherapist().getName()
                + " " + trainingSession.getPhysiotherapist().getSurname();
        String templateName = trainingSession.getExerciseTemplates().isEmpty()
                ? null
                : trainingSession.getExerciseTemplates().get(0).getName();
        List<ExerciseWebModel> exerciseWebModels = trainingSession.getExerciseTemplates().stream()
                .flatMap(template -> template.getExercises().stream())
                .map(ExerciseWebModel::fromDomainModel)
                .toList();

        return new TrainingSessionWebModel(
                trainingSession.getId().value(),
                trainingSession.getPatient().getId().value(),
                trainingSession.getStartDateTime(),
                trainingSession.getEndDateTime(),
                physioName,
                templateName,
                exerciseWebModels
        );
    }
}
