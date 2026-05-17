package com.tfg.model.pni;

import com.tfg.model.patient.PatientFactory;
import com.tfg.model.patient.Patient;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PniReportTest {

    private static final Patient TEST_PATIENT = PatientFactory.createTestPatient("hola@gmail.com", "85729487J");
    private static final PniReport TEST_PNI_REPORT = PniReportFactory.createTestPniReport(
            TEST_PATIENT,
            5.0
    );

    @Test
    public void givenValidValues_newPniReport_succeeds() {
        PniReport pniReport = TEST_PNI_REPORT;

        assertThat(pniReport.getId()).isEqualTo(TEST_PNI_REPORT.getId());
        assertThat(pniReport.getPatient()).isEqualTo(TEST_PNI_REPORT.getPatient());
        assertThat(pniReport.getReportDate()).isEqualTo(TEST_PNI_REPORT.getReportDate());
        assertThat(pniReport.getHours_asleep()).isEqualTo(TEST_PNI_REPORT.getHours_asleep());
        assertThat(pniReport.getAvg_hr()).isEqualTo(TEST_PNI_REPORT.getAvg_hr());
        assertThat(pniReport.getMin_hr()).isEqualTo(TEST_PNI_REPORT.getMin_hr());
        assertThat(pniReport.getDeep_sleep()).isEqualTo(TEST_PNI_REPORT.getDeep_sleep());
        assertThat(pniReport.getContinuity()).isEqualTo(TEST_PNI_REPORT.getContinuity());
    }

    @Test
    public void givenInvalidValues_newPniReport_throwsException() {
        try {
            new PniReport(TEST_PATIENT, -5.0, 58.0, 48, 90, 5.0);
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("Hours asleep cannot be negative");
        }

        try {
            new PniReport(TEST_PATIENT, 7.5, -1.0, 48, 90, 5.0);
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("Average HR cannot be negative");
        }

        try {
            new PniReport(TEST_PATIENT, 7.5, 58.0, -1, 90, 5.0);
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("Min HR cannot be negative");
        }

        try {
            new PniReport(TEST_PATIENT, 7.5, 58.0, 48, -1, 5.0);
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("Deep sleep cannot be negative");
        }

        try {
            new PniReport(TEST_PATIENT, 7.5, 58.0, 48, 90, 0.0);
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("Continuity must be between 1 and 5");
        }
    }
}
