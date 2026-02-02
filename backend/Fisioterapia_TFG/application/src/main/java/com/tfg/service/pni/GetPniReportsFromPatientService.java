package com.tfg.service.pni;

import com.tfg.exceptions.InvalidIdException;
import com.tfg.patient.PatientId;
import com.tfg.port.in.pni.GetPniReportsFromPatientUseCase;
import com.tfg.port.out.persistence.PatientRepository;
import com.tfg.port.out.persistence.PniReportRepository;

import java.time.LocalDate;
import java.util.List;

public class GetPniReportsFromPatientService implements GetPniReportsFromPatientUseCase {

    private final PniReportRepository pniReportRepository;
    private final PatientRepository patientRepository;

    public GetPniReportsFromPatientService(PniReportRepository pniReportRepository, PatientRepository patientRepository) {
        this.pniReportRepository = pniReportRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    public List<LocalDate> getPniReportsFromPatient(PatientId patientId) {
        patientRepository.findById(patientId)
                .orElseThrow(InvalidIdException::new);

        return pniReportRepository.findAllReportsByPatiendId(patientId);
    }
}
