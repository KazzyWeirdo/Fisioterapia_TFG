package com.tfg.model.indiba;

import com.tfg.indiba.IndibaSession;
import com.tfg.indiba.IndibaSessionId;
import com.tfg.indiba.IndibaSessionModes;
import com.tfg.patient.Patient;
import com.tfg.patient.PatientId;

import java.util.Date;

public class IndibaSessionFactory {

    public static String TREATED_AREA = "Lower Back";
    public static IndibaSessionModes MODE = IndibaSessionModes.CAPACITIVE;
    public static float INTENSITY = 5.0f;
    public static String OBJECTIVE = "Pain Relief";
    public static String PHYSIOTHERAPIST = "Dr. Smith";
    public static String OBSERVATIONS = "Patient responded well to treatment.";

    public static IndibaSession createTestIndibaSession(Patient patient, Date beginSession, Date endSession) {
        return new IndibaSession(
                patient,
                beginSession,
                endSession,
                TREATED_AREA,
                MODE,
                INTENSITY,
                OBJECTIVE,
                PHYSIOTHERAPIST,
                OBSERVATIONS
        );
    }
}
