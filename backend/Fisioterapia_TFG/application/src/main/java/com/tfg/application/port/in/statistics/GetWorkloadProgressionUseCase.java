package com.tfg.application.port.in.statistics;

import com.tfg.model.patient.PatientId;
import com.tfg.model.statistics.WorkloadProgression;

import java.util.List;

public interface GetWorkloadProgressionUseCase {
    List<WorkloadProgression> calculateProgression(PatientId patientId, String exerciseName);
}