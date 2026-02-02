package com.tfg.port.in.pni;

import com.tfg.patient.PatientId;

import java.time.LocalDate;
import java.util.List;

public interface GetPniReportsFromPatientUseCase {
    List<LocalDate> getPniReportsFromPatient(PatientId patientId);
}
