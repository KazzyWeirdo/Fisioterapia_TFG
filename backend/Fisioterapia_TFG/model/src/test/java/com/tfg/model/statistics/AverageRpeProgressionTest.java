package com.tfg.model.statistics;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class AverageRpeProgressionTest {

    @Test
    public void givenValidData_whenCreateAverageRpeProgression_thenFieldsAreAccessible() {
        LocalDate date = LocalDate.of(2024, 6, 15);
        AverageRpeProgression progression = new AverageRpeProgression(date, 7.5);

        assertEquals(date, progression.sessionDate());
        assertEquals(7.5, progression.averageRpe());
    }

    @Test
    public void givenTwoEqualInstances_whenEquals_thenTrue() {
        LocalDate date = LocalDate.of(2024, 3, 10);
        AverageRpeProgression a = new AverageRpeProgression(date, 6.0);
        AverageRpeProgression b = new AverageRpeProgression(date, 6.0);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotNull(a.toString());
    }
}
