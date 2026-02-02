package com.tfg.model.pni;

import com.tfg.model.patient.PatientFactory;
import com.tfg.patient.Patient;
import com.tfg.pni.PniReport;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class PniReportTest {

    private static final Patient TEST_PATIENT = PatientFactory.createTestPatient("hola@gmail.com", "85729487J");
    private static final PniReport TEST_PNI_REPORT = PniReportFactory.createTestPniReport(
            TEST_PATIENT,
            LocalDate.of(2026, 5, 20),
            5
    );

    @Test
    public void givenValidValues_newPniReport_succeeds() {
        PniReport pniReport = TEST_PNI_REPORT;

        assertThat(pniReport.getId()).isEqualTo(TEST_PNI_REPORT.getId());
        assertThat(pniReport.getPatient()).isEqualTo(TEST_PNI_REPORT.getPatient());
        assertThat(pniReport.getReportDate()).isEqualTo(TEST_PNI_REPORT.getReportDate());
        assertThat(pniReport.getHours_asleep()).isEqualTo(TEST_PNI_REPORT.getHours_asleep());
        assertThat(pniReport.getHrv()).isEqualTo(TEST_PNI_REPORT.getHrv());
        assertThat(pniReport.getNtrs()).isEqualTo(TEST_PNI_REPORT.getNtrs());
        assertThat(pniReport.getStress()).isEqualTo(TEST_PNI_REPORT.getStress());
    }

    @Test
    public void givenInvalidValues_newPniReport_throwsException() {
        try {
            new PniReport(
                    TEST_PATIENT,
                    LocalDate.of(2026, 5, 20),
                    -5.0,
                    65.0,
                    3,
                    5
            );
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("Hours asleep cannot be negative");
        }

        try {
            new PniReport(
                    TEST_PATIENT,
                    LocalDate.of(2026, 5, 20),
                    7.5,
                    -10.0,
                    3,
                    5
            );
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("HRV cannot be negative");
        }

        try {
            new PniReport(
                    TEST_PATIENT,
                    LocalDate.of(2026, 5, 20),
                    7.5,
                    65.0,
                    -1,
                    5
            );
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("Stress level must be between 0 and 100");
        }

        try {
            new PniReport(
                    TEST_PATIENT,
                    LocalDate.of(2026, 5, 20),
                    7.5,
                    65.0,
                    3,
                    -2
            );
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("NTRS must be between 0 and 10");
        }
    }
}
