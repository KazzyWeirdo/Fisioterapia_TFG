package com.tfg.adapter.in.rest.trainingsession;

import java.time.LocalDate;

public record TrainingSessionListWebModel(
        int id,
        LocalDate date,
        String physiotherapistName,
        String templateName
) {
}
