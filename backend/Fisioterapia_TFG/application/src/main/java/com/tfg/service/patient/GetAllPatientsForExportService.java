package com.tfg.service.patient;

import com.tfg.patient.Patient;
import com.tfg.port.in.patient.GetAllPatientsForExportUseCase;
import com.tfg.port.out.persistence.PatientRepository;

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
