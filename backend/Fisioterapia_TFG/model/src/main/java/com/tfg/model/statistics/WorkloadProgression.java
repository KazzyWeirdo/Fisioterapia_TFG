package com.tfg.model.statistics;

import java.time.LocalDate;

public record WorkloadProgression(
        LocalDate sessionDate,
        double workload
) {
}
