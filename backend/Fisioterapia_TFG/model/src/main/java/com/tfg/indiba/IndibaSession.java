package com.tfg.indiba;

import com.tfg.patient.Patient;
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
    private final Patient patient;
    private Date beginSession;
    private Date endSession;
    private String treatedArea;
    private IndibaSessionModes mode;
    private float intensity;
    private String objective;
    private String physiotherapist; // TODO: Change to PhysiotherapistId when the class is created
    private String observations;

    public IndibaSession(Patient patient, Date beginSession, Date endSession, String treatedArea, IndibaSessionModes mode, float intensity, String objective, String physiotherapist, String observations) {
        checkDates(beginSession, endSession);
        checkIntensity(intensity);
        this.id = new IndibaSessionId(ThreadLocalRandom.current().nextInt(1_000_000));
        this.patient = patient;
        this.beginSession = beginSession;
        this.endSession = endSession;
        this.treatedArea = treatedArea;
        this.mode = mode;
        this.intensity = intensity;
        this.objective = objective;
        this.physiotherapist = physiotherapist;
        this.observations = observations;
    }

    private void checkDates(Date begin, Date end) {
        if (begin.after(end)) {
            throw new IllegalArgumentException("Begin date cannot be after end date");
        }
    }

    private void checkIntensity(float intensity) {
        if (intensity < 0) {
            throw new IllegalArgumentException("Intensity must be greater than 0");
        }
    }

}
