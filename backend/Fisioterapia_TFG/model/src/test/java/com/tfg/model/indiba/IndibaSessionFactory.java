package com.tfg.model.indiba;

import com.tfg.indiba.IndibaSession;
import com.tfg.indiba.IndibaSessionId;
import com.tfg.indiba.IndibaSessionModes;
import com.tfg.patient.PatientId;

import java.util.Date;

public class IndibaSessionFactory {

    public static final IndibaSessionId INDIBA_SESSION_ID = new IndibaSessionId(1);
    public static PatientId PATIENT_ID = new PatientId(1);
    public static Date BEGIN_SESSION = new Date();
    public static Date END_SESSION = new Date();
    public static String TREATED_AREA = "Lower Back";
    public static IndibaSessionModes MODE = IndibaSessionModes.CAPACITIVE;
    public static float INTENSITY = 5.0f;
    public static String OBJECTIVE = "Pain Relief";
    public static String PHYSIOTHERAPIST = "Dr. Smith";
    public static String OBSERVATIONS = "Patient responded well to treatment.";

    public static IndibaSession createTestIndibaSession(int sessionId, int patientId, Date beginSession, Date endSession) {
        return new IndibaSession(
                new IndibaSessionId(sessionId),
                new PatientId(patientId),
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
