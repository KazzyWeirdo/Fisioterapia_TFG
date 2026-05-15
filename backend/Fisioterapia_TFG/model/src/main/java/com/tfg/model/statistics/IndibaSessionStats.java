package com.tfg.model.statistics;

import java.util.Map;

public record IndibaSessionStats(
        int totalSessions,
        double avgDurationMinutes,
        String mostTreatedArea,
        Double avgCapacitiveIntensity,
        Double avgResistiveIntensity,
        Map<String, Long> modeDistribution
) {}
