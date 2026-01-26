package com.tfg.indiba;

import com.tfg.patient.PatientId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

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
}
