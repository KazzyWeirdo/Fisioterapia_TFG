package com.tfg.application.service.polar;

import com.tfg.application.exceptions.PatientNotFoundException;
import com.tfg.application.port.in.polar.SyncPolarDataForPatientUseCase;
import com.tfg.application.port.out.persistence.PatientRepository;
import com.tfg.application.port.out.persistence.PniReportRepository;
import com.tfg.application.port.out.polar.PolarRepository;
import com.tfg.model.patient.Patient;
import com.tfg.model.patient.PatientId;
import java.time.LocalDate;

public class SyncPolarDataForPatientService implements SyncPolarDataForPatientUseCase {

    private final PatientRepository patientRepository;
    private final PniReportRepository pniReportRepository;
    private final PolarRepository polarRepository;

    public SyncPolarDataForPatientService(PatientRepository patientRepository,
                                          PniReportRepository pniReportRepository,
                                          PolarRepository polarRepository) {
        this.patientRepository = patientRepository;
        this.pniReportRepository = pniReportRepository;
        this.polarRepository = polarRepository;
    }

    @Override
    public void syncForPatient(PatientId patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException(patientId));

        LocalDate today = LocalDate.now();
        polarRepository.fetchDailyData(patient, today).ifPresent(fresh -> {
            pniReportRepository.findByPatientIdAndDate(patientId, today)
                    .ifPresentOrElse(existing -> {
                        existing.setHours_asleep(fresh.getHours_asleep());
                        existing.setAvg_hr(fresh.getAvg_hr());
                        existing.setMin_hr(fresh.getMin_hr());
                        existing.setDeep_sleep(fresh.getDeep_sleep());
                        existing.setContinuity(fresh.getContinuity());
                        pniReportRepository.save(existing);
                    }, () -> pniReportRepository.save(fresh));
        });
    }
}
