package com.tfg.model.indiba;

import com.tfg.indiba.IndibaSession;
import com.tfg.indiba.IndibaSessionModes;
import com.tfg.model.patient.PatientFactory;
import com.tfg.model.physiotherapist.PhysiotherapistFactory;
import com.tfg.patient.Patient;
import com.tfg.physiotherapist.Physiotherapist;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class IndibaSessionTest {

    private static final Patient TEST_PATIENT = PatientFactory.createTestPatient("hola@gmail.com", "85729487J");
    private static final Physiotherapist TEST_PHYSIOTHERAPIST = PhysiotherapistFactory.createTestPsychiatrist("hola@gmail.com", "ValidPassword123!");
    private static final IndibaSession INDIBA_SESSION = IndibaSessionFactory.createTestIndibaSession(
            TEST_PATIENT,
            TEST_PHYSIOTHERAPIST,
            new Date("2023/01/01"),
            new Date("2023/01/02"));

    @Test
    public void givenValidValues_newIndibaSession_succeeds() {
        IndibaSession indibaSession = INDIBA_SESSION;

        assertThat(indibaSession.getId()).isEqualTo(INDIBA_SESSION.getId());
        assertThat(indibaSession.getPatient()).isEqualTo(INDIBA_SESSION.getPatient());
        assertThat(indibaSession.getBeginSession()).isEqualTo(INDIBA_SESSION.getBeginSession());
        assertThat(indibaSession.getEndSession()).isEqualTo(INDIBA_SESSION.getEndSession());
        assertThat(indibaSession.getTreatedArea()).isEqualTo(INDIBA_SESSION.getTreatedArea());
        assertThat(indibaSession.getMode()).isEqualTo(INDIBA_SESSION.getMode());
        assertThat(indibaSession.getCapacitiveIntensity()).isEqualTo(INDIBA_SESSION.getCapacitiveIntensity());
        assertThat(indibaSession.getObjective()).isEqualTo(INDIBA_SESSION.getObjective());
        assertThat(indibaSession.getPhysiotherapist()).isEqualTo(INDIBA_SESSION.getPhysiotherapist());
        assertThat(indibaSession.getObservations()).isEqualTo(INDIBA_SESSION.getObservations());
    }

    @Test
    public void givenDualModeWithNegativeCapacitiveIntensity_newIndibaSession_throws() {
        assertThatThrownBy(() -> new IndibaSession(
                TEST_PATIENT, new Date("2023/01/01"), new Date("2023/01/02"),
                "Lower Back", IndibaSessionModes.DUAL, -1.0f, 5.0f,
                "Objective", TEST_PHYSIOTHERAPIST, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void givenDualModeWithNegativeResistiveIntensity_newIndibaSession_throws() {
        assertThatThrownBy(() -> new IndibaSession(
                TEST_PATIENT, new Date("2023/01/01"), new Date("2023/01/02"),
                "Lower Back", IndibaSessionModes.DUAL, 5.0f, -1.0f,
                "Objective", TEST_PHYSIOTHERAPIST, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void givenCapacitiveModeWithResistiveIntensityZero_newIndibaSession_succeeds() {
        IndibaSession session = new IndibaSession(
                TEST_PATIENT, new Date("2023/01/01"), new Date("2023/01/02"),
                "Lower Back", IndibaSessionModes.CAPACITIVE, 5.0f, 0.0f,
                "Objective", TEST_PHYSIOTHERAPIST, null);
        assertThat(session.getCapacitiveIntensity()).isEqualTo(5.0f);
    }

    @Test
    public void givenResistiveModeWithCapacitiveIntensityZero_newIndibaSession_succeeds() {
        IndibaSession session = new IndibaSession(
                TEST_PATIENT, new Date("2023/01/01"), new Date("2023/01/02"),
                "Lower Back", IndibaSessionModes.RESISTIVE, 0.0f, 5.0f,
                "Objective", TEST_PHYSIOTHERAPIST, null);
        assertThat(session.getResistiveIntensity()).isEqualTo(5.0f);
    }
}
