package com.tfg.indiba;

import com.tfg.patient.Patient;
import com.tfg.physiotherapist.Physiotherapist;
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
    private Float capacitiveIntensity;
    private Float resistiveIntensity;
    private String objective;
    private Physiotherapist physiotherapist;
    private String observations;

    public IndibaSession(Patient patient, Date beginSession, Date endSession, String treatedArea, IndibaSessionModes mode, Float capacitiveIntensity, Float resistiveIntensity, String objective, Physiotherapist physiotherapist, String observations) {
        checkDates(beginSession, endSession);
        checkIntensities(mode, capacitiveIntensity, resistiveIntensity);
        this.id = new IndibaSessionId(ThreadLocalRandom.current().nextInt(1_000_000));
        this.patient = patient;
        this.beginSession = beginSession;
        this.endSession = endSession;
        this.treatedArea = treatedArea;
        this.mode = mode;
        this.capacitiveIntensity = capacitiveIntensity;
        this.resistiveIntensity = resistiveIntensity;
        this.objective = objective;
        this.physiotherapist = physiotherapist;
        this.observations = observations;
    }

    private void checkDates(Date begin, Date end) {
        if (begin.after(end)) {
            throw new IllegalArgumentException("Begin date cannot be after end date");
        }
    }

    private void checkIntensities(IndibaSessionModes mode, Float cap, Float res) {
        switch (mode) {
            case CAPACITIVE -> {
                if (cap == null || cap < 0) throw new IllegalArgumentException("Capacitive intensity is required and must be >= 0");
            }
            case RESISTIVE -> {
                if (res == null || res < 0) throw new IllegalArgumentException("Resistive intensity is required and must be >= 0");
            }
            case DUAL -> {
                if (cap == null || cap < 0) throw new IllegalArgumentException("Capacitive intensity is required for DUAL mode and must be >= 0");
                if (res == null || res < 0) throw new IllegalArgumentException("Resistive intensity is required for DUAL mode and must be >= 0");
            }
        }
    }

}
