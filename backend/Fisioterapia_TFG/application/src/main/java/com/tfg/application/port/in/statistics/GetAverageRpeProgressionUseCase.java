package com.tfg.application.port.in.statistics;

import com.tfg.model.patient.PatientId;
import com.tfg.model.statistics.AverageRpeProgression;

import java.util.List;

public interface GetAverageRpeProgressionUseCase {
    List<AverageRpeProgression> calculateProgression(PatientId patientId, String exerciseName);
}