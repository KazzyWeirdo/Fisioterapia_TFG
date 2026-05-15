package com.tfg.application.service.patient;

import com.tfg.model.patient.Patient;
import com.tfg.application.port.in.patient.GetAllPatientsForExportUseCase;
import com.tfg.application.port.out.persistence.PatientRepository;

import java.util.List;

public class GetAllPatientsForExportService implements GetAllPatientsForExportUseCase {

    private final PatientRepository patientRepository;

    public GetAllPatientsForExportService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public List<Patient> getAllPatientsForExport() {
        return patientRepository.findAll();
    }
}
