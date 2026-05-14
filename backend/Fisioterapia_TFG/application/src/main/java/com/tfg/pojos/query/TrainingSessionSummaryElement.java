package com.tfg.pojos.query;

import java.time.LocalDateTime;

public record TrainingSessionSummaryElement(
        int id,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        String physiotherapistName,
        String templateName
) {
}
