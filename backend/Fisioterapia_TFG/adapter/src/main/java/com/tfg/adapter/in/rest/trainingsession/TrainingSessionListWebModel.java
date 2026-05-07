package com.tfg.adapter.in.rest.trainingsession;

import java.time.LocalDateTime;

public record TrainingSessionListWebModel(
        int id,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        String physiotherapistName,
        String templateName
) {
}
