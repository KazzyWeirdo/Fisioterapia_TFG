package com.tfg.adapter.in.rest.trainingsession;

import java.time.LocalDate;

public record TrainingSessionListWebModel(
        int id,
        LocalDate date
) {
    static TrainingSessionListWebModel fromDomainModel(com.tfg.trainingsession.TrainingSession trainingSession) {
        return new TrainingSessionListWebModel(
                trainingSession.getId().value(),
                trainingSession.getDate()
        );
    }
}
