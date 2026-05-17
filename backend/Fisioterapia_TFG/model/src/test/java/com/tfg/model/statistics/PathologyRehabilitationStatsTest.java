package com.tfg.model.statistics;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PathologyRehabilitationStatsTest {

    @Test
    public void givenValidData_whenCreatePathologyStats_thenFieldsAreAccessible() {
        PathologyRehabilitationStats stats = new PathologyRehabilitationStats("TENDINOPATHY", 30.5, 12);

        assertEquals("TENDINOPATHY", stats.pathology());
        assertEquals(30.5, stats.averageDaysToDischarge());
        assertEquals(12, stats.sampleSize());
    }

    @Test
    public void givenTwoEqualInstances_whenEquals_thenTrue() {
        PathologyRehabilitationStats a = new PathologyRehabilitationStats("LUMBAR_PAIN", 45.0, 5);
        PathologyRehabilitationStats b = new PathologyRehabilitationStats("LUMBAR_PAIN", 45.0, 5);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotNull(a.toString());
    }
}
