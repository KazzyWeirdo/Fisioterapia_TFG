package com.tfg.port.in.indiba;

import com.tfg.indiba.IndibaSession;
import com.tfg.patient.PatientId;

import java.util.Date;
import java.util.List;

public interface GetIndibaSessionFromPatientUseCase {
    List<Date> getIndibaSessionsFromPatient(PatientId patientId);
}
