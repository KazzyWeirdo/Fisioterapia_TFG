package com.tfg.port.in.trainingsession;

import com.tfg.patient.PatientId;

import java.time.LocalDate;
import java.util.List;

public interface GetTrainingSessionByPatientUseCase {

    List<LocalDate> getTrainingSessionFromPatient(PatientId patientId);
}
