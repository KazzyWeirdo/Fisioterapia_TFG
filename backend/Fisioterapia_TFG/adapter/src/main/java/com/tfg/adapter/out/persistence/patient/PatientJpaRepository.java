package com.tfg.adapter.out.persistence.patient;

import com.tfg.patient.Patient;
import com.tfg.patient.PatientId;
import com.tfg.pojos.pagedpojos.PageQuery;
import com.tfg.pojos.pagedpojos.PagedResponse;
import com.tfg.pojos.query.PatientSummaryElement;
import com.tfg.port.out.persistence.PatientRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
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
                patient.getGenderIdentity(),
                patient.getAdministrativeSex(),
                patient.getLegalName(),
                patient.getNameToUse(),
                patient.getSurname(),
                patient.getSecondSurname(),
                patient.getPronouns(),
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

    @Override
    public PagedResponse<PatientSummaryElement> findAllSummaries(PageQuery query) {
        Pageable pageable = PageRequest.of(query.page(), query.size());

        Page<PatientSummaryJpaProjection> page = patientJpaDataRepository.findSummaries(pageable);

        List<PatientSummaryElement> content = page.getContent().stream()
                .map(proj -> new PatientSummaryElement(proj.id(), proj.name(), proj.surname()))
                .toList();

        return new PagedResponse<>(
                content,
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.isLast()
        );
    }

    @Override
    public List<Patient> findAllWithPolarToken() {
        List<PatientJpaEntity> patientJpaEntities = patientJpaDataRepository.findAllWithPolarToken();
        return patientJpaEntities.stream().map(PatientJpaMapper::toModelEntity).toList();
    }
}
