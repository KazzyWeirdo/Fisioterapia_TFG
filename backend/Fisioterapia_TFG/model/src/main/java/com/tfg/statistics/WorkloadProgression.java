package com.tfg.statistics;

import java.time.LocalDate;

public record WorkloadProgression(
        LocalDate sessionDate,
        double workload
) {
}
