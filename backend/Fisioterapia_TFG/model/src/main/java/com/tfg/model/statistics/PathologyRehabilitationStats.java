package com.tfg.model.statistics;

public record PathologyRehabilitationStats(
        String pathology,
        double averageDaysToDischarge,
        int sampleSize
) {}
