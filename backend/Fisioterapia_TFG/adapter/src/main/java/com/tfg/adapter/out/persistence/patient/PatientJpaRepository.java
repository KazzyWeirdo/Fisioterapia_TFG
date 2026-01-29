package com.tfg.adapter.out.persistence.patient;

import com.tfg.patient.Patient;
import com.tfg.patient.PatientId;
import com.tfg.port.out.persistence.PatientRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PatientJpaRepository implements PatientRepository {

    private final PatientJpaDataRepository patientJpaDataRepository;

    public PatientJpaRepository(PatientJpaDataRepository patientJpaDataRepository) {
        this.patientJpaDataRepository = patientJpaDataRepository;
    }

    @Override
    @Transactional
    public void save(Patient patient) {
        patientJpaDataRepository.save(PatientJpaMapper.toJpaEntity(patient));
    }

    @Override
    @Transactional
    public void deleteAll() {
        patientJpaDataRepository.deleteAll();
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public void update(PatientId patientId, Patient patient) {
        patientJpaDataRepository.updatePatientById(
                patientId.value(),
                patient.getEmail().value(),
                patient.getDni().value(),
                patient.getGender(),
                patient.getName(),
                patient.getSurname(),
                patient.getSecondSurname(),
                patient.getDateOfBirth(),
                patient.getPhoneNumber()
        );
    }

    @Override
    public Optional<Patient> findById(PatientId id) {
        Optional<PatientJpaEntity> patientJpaEntity = patientJpaDataRepository.findById(id.value());
        return patientJpaEntity.map(PatientJpaMapper::toModelEntity);
    }

    @Override
    public Optional<Patient> findByEmail(com.tfg.patient.PatientEmail email) {
        Optional<PatientJpaEntity> patientJpaEntity = patientJpaDataRepository.findByEmail(email.value());
        return patientJpaEntity.map(PatientJpaMapper::toModelEntity);
    }

    @Override
    public Optional<Patient> findByDni(com.tfg.patient.PatientDNI dni) {
        Optional<PatientJpaEntity> patientJpaEntity = patientJpaDataRepository.findByDni(dni.value());
        return patientJpaEntity.map(PatientJpaMapper::toModelEntity);
    }
}
