package com.tfg.adapter.in.rest.statistics;

import com.tfg.model.statistics.IndibaSessionStats;

import java.util.Map;

public record IndibaSessionStatsWebModel(
        int totalSessions,
        double avgDurationMinutes,
        String mostTreatedArea,
        Double avgCapacitiveIntensity,
        Double avgResistiveIntensity,
        Map<String, Long> modeDistribution
) {
    public static IndibaSessionStatsWebModel from(IndibaSessionStats stats) {
        return new IndibaSessionStatsWebModel(
                stats.totalSessions(),
                stats.avgDurationMinutes(),
                stats.mostTreatedArea(),
                stats.avgCapacitiveIntensity(),
                stats.avgResistiveIntensity(),
                stats.modeDistribution()
        );
    }
}
