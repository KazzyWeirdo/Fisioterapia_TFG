package com.tfg.adapter.in.rest.statistics;

import com.tfg.model.statistics.PathologyRehabilitationStats;

public record PathologyRehabilitationStatsWebModel(
        String pathology,
        double averageDaysToDischarge,
        int sampleSize
) {
    public static PathologyRehabilitationStatsWebModel from(PathologyRehabilitationStats stats) {
        return new PathologyRehabilitationStatsWebModel(
                stats.pathology(),
                stats.averageDaysToDischarge(),
                stats.sampleSize()
        );
    }
}
