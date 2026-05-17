package com.tfg.application.service.polar;

import com.tfg.model.patient.Patient;
import java.time.LocalDate;
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

        LocalDate yesterday = LocalDate.now().minusDays(1);
        for (Patient patient : patients) {
            try {
                polarRepository.fetchDailyData(patient, yesterday).ifPresent(fresh -> {
                    pniReportRepository.findByPatientIdAndDate(patient.getId(), yesterday)
                            .ifPresentOrElse(existing -> {
                                existing.setHours_asleep(fresh.getHours_asleep());
                                existing.setAvg_hr(fresh.getAvg_hr());
                                existing.setMin_hr(fresh.getMin_hr());
                                existing.setDeep_sleep(fresh.getDeep_sleep());
                                existing.setContinuity(fresh.getContinuity());
                                pniReportRepository.save(existing);
                            }, () -> pniReportRepository.save(fresh));
                });
            } catch (Exception e) {
                System.err.println("Error sync user " + patient.getId() + ": " + e.getMessage());
            }
        }
    }
}
