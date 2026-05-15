package com.tfg.application.service.polar;

import com.tfg.model.patient.Patient;
import com.tfg.application.port.in.polar.SyncPolarDataUseCase;
import com.tfg.application.port.out.persistence.PatientRepository;
import com.tfg.application.port.out.persistence.PniReportRepository;
import com.tfg.application.port.out.polar.PolarRepository;

import java.util.List;

public class SyncPolarDataService implements SyncPolarDataUseCase {

    private final PatientRepository patientRepository;
    private final PniReportRepository pniReportRepository;
    private final PolarRepository polarRepository;

    public SyncPolarDataService(PatientRepository patientRepository, PniReportRepository pniReportRepository,
        PolarRepository polarRepository) {

        this.patientRepository = patientRepository;
        this.polarRepository = polarRepository;
        this.pniReportRepository = pniReportRepository;
    }

    @Override
    public void executeDailySync() {
        List<Patient> patients = patientRepository.findAllWithPolarToken();

        for (Patient patient : patients) {
            try {
                polarRepository.fetchDailyData(patient)
                        .ifPresent(data -> {
                            pniReportRepository.save(data);
                        });
            } catch (Exception e) {
                System.err.println("Error sync user " + patient.getId() + ": " + e.getMessage());
            }
        }
    }
}
