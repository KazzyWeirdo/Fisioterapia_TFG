package com.tfg.adapter.in.rest.trainingsession;

import java.time.LocalDate;

public record TrainingSessionListWebModel(
        String patientId,
        LocalDate date
) {
    static TrainingSessionListWebModel fromDomainModel(com.tfg.trainingsession.TrainingSession trainingSession) {
        return new TrainingSessionListWebModel(
                String.valueOf(trainingSession.getPatient().getId().value()),
                trainingSession.getDate()
        );
    }
}
