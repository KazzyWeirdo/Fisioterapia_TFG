package com.tfg.port.in.statistics;

import com.tfg.patient.PatientId;
import com.tfg.statistics.WorkloadProgression;

import java.util.List;

public interface GetWorkloadProgressionUseCase {
    List<WorkloadProgression> calculateProgression(PatientId patientId, String exerciseName);
}