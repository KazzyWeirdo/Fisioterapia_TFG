package com.tfg.model.indiba;

import com.tfg.model.physiotherapist.PhysiotherapistFactory;
import com.tfg.model.patient.Patient;
import com.tfg.model.physiotherapist.Physiotherapist;

import java.util.Date;

public class IndibaSessionFactory {

    public static String TREATED_AREA = "Lower Back";
    public static IndibaSessionModes MODE = IndibaSessionModes.CAPACITIVE;
    public static Float CAPACITIVE_INTENSITY = 5.0f;
    public static Float RESISTIVE_INTENSITY = null;
    public static String OBSERVATIONS = "Patient responded well to treatment.";

    public static IndibaSession createTestIndibaSession(Patient patient, Physiotherapist physiotherapist, Date beginSession, Date endSession) {
        return new IndibaSession(
                patient,
                beginSession,
                endSession,
                TREATED_AREA,
                MODE,
                CAPACITIVE_INTENSITY,
                RESISTIVE_INTENSITY,
                physiotherapist,
                OBSERVATIONS
        );
    }

    public static IndibaSession createTestIndibaSession(Patient patient) {
        Physiotherapist physiotherapist = PhysiotherapistFactory.createTestPsychiatrist(
                "physio_" + System.nanoTime() + "@test.com", "ValidPassword123!");
        Date begin = new Date(System.currentTimeMillis() - 3600_000);
        Date end = new Date(System.currentTimeMillis());
        return new IndibaSession(
                patient,
                begin,
                end,
                TREATED_AREA,
                MODE,
                CAPACITIVE_INTENSITY,
                RESISTIVE_INTENSITY,
                physiotherapist,
                OBSERVATIONS
        );
    }
}
