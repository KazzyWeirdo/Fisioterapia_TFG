package com.tfg.port.in.patient;

import com.tfg.patient.Patient;

import java.util.List;

public interface GetAllPatientsForExportUseCase {
    List<Patient> getAllPatientsForExport();
}
