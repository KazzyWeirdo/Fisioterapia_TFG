package com.tfg.port.in.statistics;

import com.tfg.patient.PatientId;
import com.tfg.statistics.PatientMonthTransitionRatio;

import java.util.List;

public interface GetPatientTransitionRatioUseCase {
    List<PatientMonthTransitionRatio> getTransitionRatio(PatientId id, Integer year);
}
