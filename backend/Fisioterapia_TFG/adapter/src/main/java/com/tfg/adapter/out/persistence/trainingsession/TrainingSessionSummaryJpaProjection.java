package com.tfg.adapter.out.persistence.trainingsession;

import java.time.LocalDateTime;

public record TrainingSessionSummaryJpaProjection(
        int id,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        String physiotherapistFirstName,
        String physiotherapistSurname,
        String templateName
) {
}
