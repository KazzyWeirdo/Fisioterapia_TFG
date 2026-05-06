package com.tfg.pojos.query;

import java.time.LocalDate;

public record TrainingSessionSummaryElement(
        int id,
        LocalDate date,
        String physiotherapistName,
        String templateName
) {
}
