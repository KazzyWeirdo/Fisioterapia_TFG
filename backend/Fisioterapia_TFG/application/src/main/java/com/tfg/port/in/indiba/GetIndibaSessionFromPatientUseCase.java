package com.tfg.port.in.indiba;

import com.tfg.indiba.IndibaSession;
import com.tfg.patient.PatientId;

import java.util.List;

public interface GetIndibaSessionFromPatientUseCase {
    List<IndibaSession> getIndibaSessionsFromPatient(PatientId patientId);
}
