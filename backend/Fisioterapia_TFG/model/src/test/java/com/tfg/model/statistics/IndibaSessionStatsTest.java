package com.tfg.model.statistics;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class IndibaSessionStatsTest {

    @Test
    public void givenValidData_whenCreateIndibaSessionStats_thenFieldsAreAccessible() {
        Map<String, Long> modeDist = Map.of("CAPACITIVE", 3L, "RESISTIVE", 1L);
        IndibaSessionStats stats = new IndibaSessionStats(4, 45.5, "Lower Back", 5.0, 3.0, modeDist);

        assertEquals(4, stats.totalSessions());
        assertEquals(45.5, stats.avgDurationMinutes());
        assertEquals("Lower Back", stats.mostTreatedArea());
        assertEquals(5.0, stats.avgCapacitiveIntensity());
        assertEquals(3.0, stats.avgResistiveIntensity());
        assertEquals(2, stats.modeDistribution().size());
        assertEquals(3L, stats.modeDistribution().get("CAPACITIVE"));
    }

    @Test
    public void givenNullIntensities_whenCreateIndibaSessionStats_thenNullsAreAllowed() {
        IndibaSessionStats stats = new IndibaSessionStats(0, 0.0, null, null, null, Map.of());

        assertEquals(0, stats.totalSessions());
        assertNull(stats.mostTreatedArea());
        assertNull(stats.avgCapacitiveIntensity());
        assertNull(stats.avgResistiveIntensity());
        assertTrue(stats.modeDistribution().isEmpty());
    }

    @Test
    public void givenTwoEqualInstances_whenEquals_thenTrue() {
        Map<String, Long> dist = Map.of("DUAL", 2L);
        IndibaSessionStats a = new IndibaSessionStats(2, 30.0, "Knee", 4.0, 4.0, dist);
        IndibaSessionStats b = new IndibaSessionStats(2, 30.0, "Knee", 4.0, 4.0, dist);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotNull(a.toString());
    }
}
