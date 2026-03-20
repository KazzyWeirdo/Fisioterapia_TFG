package com.tfg.port.in.pni;

import com.tfg.patient.PatientId;
import com.tfg.pni.PniReport;

import java.util.List;

public interface GetPniReportsFromPatientUseCase {
    List<PniReport> getPniReportsFromPatient(PatientId patientId);
}
