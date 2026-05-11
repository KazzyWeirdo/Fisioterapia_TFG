package com.tfg.application.port.in.statistics;

import com.tfg.model.patient.PatientId;
import com.tfg.model.statistics.IndibaSessionStats;

public interface GetIndibaSessionStatsUseCase {
    IndibaSessionStats getIndibaSessionStats(PatientId patientId);
}
