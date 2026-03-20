package com.tfg.port.in.trainingsession;

import com.tfg.patient.PatientId;
import com.tfg.trainingsession.TrainingSession;

import java.util.List;

public interface GetTrainingSessionByPatientUseCase {

    List<TrainingSession> getTrainingSessionFromPatient(PatientId patientId);
}
