package com.tfg.application.port.in.patient;

import com.tfg.model.patient.Patient;

import java.util.List;

public interface GetAllPatientsForExportUseCase {
    List<Patient> getAllPatientsForExport();
}
