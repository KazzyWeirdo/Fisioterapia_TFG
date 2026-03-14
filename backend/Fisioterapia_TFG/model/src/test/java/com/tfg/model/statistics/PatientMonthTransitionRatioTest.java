package com.tfg.model.statistics;

import com.tfg.statistics.PatientMonthTransitionRatio;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class PatientMonthTransitionRatioTest {

    @ParameterizedTest
    @CsvSource({
        "0, 0, 0.0",
        "5, 5, 0.5",
        "10, 0, 0.0",
        "0, 10, 1.0",
        "3, 7, 0.7",
        "7, 3, 0.3",
    })
    void givenSessions_calculateTransitionRatio_returnsExpected(int indibaSessions, int trainingSessions, double expectedRatio) {
        PatientMonthTransitionRatio ratio = new PatientMonthTransitionRatio(1, 2024, indibaSessions, trainingSessions);

        double actualRatio = ratio.getTransitionRatio();

        assert Math.abs(actualRatio - expectedRatio) < 0.0001 :
            String.format("Expected: %.4f, Actual: %.4f", expectedRatio, actualRatio);
    }
}
