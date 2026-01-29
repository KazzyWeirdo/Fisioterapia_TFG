package com.tfg.model.indiba;

import com.tfg.indiba.IndibaSession;
import com.tfg.model.patient.PatientFactory;
import com.tfg.patient.Patient;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.assertThat;

public class IndibaSessionTest {

    private static final Patient TEST_PATIENT = PatientFactory.createTestPatient("hola@gmail.com", "85729487J");
    private static final IndibaSession INDIBA_SESSION = IndibaSessionFactory.createTestIndibaSession(
            TEST_PATIENT,
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
        assertThat(indibaSession.getIntensity()).isEqualTo(INDIBA_SESSION.getIntensity());
        assertThat(indibaSession.getObjective()).isEqualTo(INDIBA_SESSION.getObjective());
        assertThat(indibaSession.getPhysiotherapist()).isEqualTo(INDIBA_SESSION.getPhysiotherapist());
        assertThat(indibaSession.getObservations()).isEqualTo(INDIBA_SESSION.getObservations());
    }
}
