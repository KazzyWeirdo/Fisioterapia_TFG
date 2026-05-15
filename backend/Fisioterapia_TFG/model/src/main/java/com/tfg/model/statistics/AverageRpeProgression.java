package com.tfg.model.statistics;

import java.time.LocalDate;

public record AverageRpeProgression(
        LocalDate sessionDate,
        double averageRpe
) {
}
