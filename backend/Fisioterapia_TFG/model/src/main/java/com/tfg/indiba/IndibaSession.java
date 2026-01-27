package com.tfg.indiba;

import com.tfg.patient.Patient;
import com.tfg.patient.PatientId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

@AllArgsConstructor
@Getter
@Setter
public class IndibaSession {
    private final IndibaSessionId id;
    private final PatientId patientId;
    private Date beginSession;
    private Date endSession;
    private String treatedArea;
    private IndibaSessionModes mode;
    private float intensity;
    private String objective;
    private String physiotherapist; // TODO: Change to PhysiotherapistId when the class is created
    private String observations;

    public IndibaSession(int patientId, Date beginSession, Date endSession, String treatedArea, IndibaSessionModes mode, float intensity, String objective, String physiotherapist, String observations) {
        this.id = new IndibaSessionId(ThreadLocalRandom.current().nextInt(1_000_000));
        this.patientId = new PatientId(patientId);
        this.beginSession = beginSession;
        this.endSession = endSession;
        this.treatedArea = treatedArea;
        this.mode = mode;
        this.intensity = intensity;
        this.objective = objective;
        this.physiotherapist = physiotherapist;
        this.observations = observations;
    }
}
