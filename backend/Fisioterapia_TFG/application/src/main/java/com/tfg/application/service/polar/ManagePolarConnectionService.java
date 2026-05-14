package com.tfg.application.service.polar;


import com.tfg.application.exceptions.InvalidIdException;
import com.tfg.model.patient.Patient;
import com.tfg.model.patient.PatientId;
import com.tfg.application.port.in.polar.ManagePolarConnectionUseCase;
import com.tfg.application.port.out.persistence.PatientRepository;
import com.tfg.application.port.out.polar.PolarRepository;

public class ManagePolarConnectionService implements ManagePolarConnectionUseCase {

    private final PolarRepository polarRepository;
    private final PatientRepository patientRepository;

    public ManagePolarConnectionService(PolarRepository polarRepository, PatientRepository patientRepository) {
        this.polarRepository = polarRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    public String initiateConnection(PatientId patientId) {
        patientRepository.findById(patientId)
                .orElseThrow(InvalidIdException::new);

        return polarRepository.generateAuthUrl(String.valueOf(patientId.value()));
    }

    @Override
    public void finalizeConnection(String code, PatientId patientId) {
        PolarRepository.PolarAuthResult result = polarRepository.exchangeCode(code);

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(InvalidIdException::new);

        patient.setPolarAccessToken(result.accessToken());
        patient.setPolarUserId(result.polarUserId());

        patientRepository.save(patient);

        polarRepository.registerUserInPolar(result.accessToken(), result.polarUserId());
    }
}
