package com.tfg.model.indiba;

import com.tfg.indiba.IndibaSession;
import com.tfg.indiba.IndibaSessionId;
import com.tfg.indiba.IndibaSessionModes;
import com.tfg.patient.Patient;
import com.tfg.physiotherapist.Physiotherapist;

import java.util.Date;

public class IndibaSessionFactory {

    public static String TREATED_AREA = "Lower Back";
    public static IndibaSessionModes MODE = IndibaSessionModes.CAPACITIVE;
    public static Float CAPACITIVE_INTENSITY = 5.0f;
    public static Float RESISTIVE_INTENSITY = null;
    public static String OBJECTIVE = "Pain Relief";
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
                OBJECTIVE,
                physiotherapist,
                OBSERVATIONS
        );
    }
}
