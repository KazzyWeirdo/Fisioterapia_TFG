package com.tfg.adapter.out.persistence.trainingsession;

import java.time.LocalDate;

public record TrainingSessionSummaryJpaProjection(
        int id,
        LocalDate date
) {
}
