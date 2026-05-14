package com.tfg.application.port.in.statistics;

import com.tfg.model.patient.PatientId;
import com.tfg.model.statistics.PatientMonthTransitionRatio;

import java.util.List;

public interface GetPatientTransitionRatioUseCase {
    List<PatientMonthTransitionRatio> getTransitionRatio(PatientId id, Integer year);
}
