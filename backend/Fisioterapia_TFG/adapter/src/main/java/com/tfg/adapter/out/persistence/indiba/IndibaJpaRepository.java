package com.tfg.adapter.out.persistence.indiba;

import com.tfg.adapter.out.persistence.patient.PatientJpaEntity;
import com.tfg.adapter.out.persistence.patient.PatientJpaMapper;
import com.tfg.adapter.out.persistence.physiotherapist.PhysiotherapistJpaEntity;
import com.tfg.adapter.out.persistence.physiotherapist.PhysiotherapistJpaMapper;
import com.tfg.model.indiba.IndibaSession;
import com.tfg.model.indiba.IndibaSessionId;
import com.tfg.model.patient.PatientId;
import com.tfg.application.pojos.pagedpojos.PageQuery;
import com.tfg.application.pojos.pagedpojos.PagedResponse;
import com.tfg.application.pojos.query.IndibaSummaryElement;
import com.tfg.application.port.out.persistence.IndibaSessionRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class IndibaJpaRepository implements IndibaSessionRepository {

    private final IndibaJpaDataRepository indibaJpaDataRepository;

    public IndibaJpaRepository(IndibaJpaDataRepository indibaJpaDataRepository) {
        this.indibaJpaDataRepository = indibaJpaDataRepository;
    }


    @Override
    @Transactional
    public void save(IndibaSession indibaSession) {
        PatientJpaEntity patientJpaEntity = PatientJpaMapper.toJpaEntity(indibaSession.getPatient());
        PhysiotherapistJpaEntity physiotherapistJpaEntity = PhysiotherapistJpaMapper.toJpaEntity(indibaSession.getPhysiotherapist());
        indibaJpaDataRepository.save(IndibaJpaMapper.toJpaEntity(patientJpaEntity, physiotherapistJpaEntity, indibaSession));
    }

    @Override
    @Transactional
    public void deleteAll() {
        indibaJpaDataRepository.deleteAll();
    }

    @Override
    @Transactional
    public void deleteAllByPatientId(PatientId patientId) {
        indibaJpaDataRepository.deleteByPatientId(patientId.value());
    }

    @Override
    public Optional<IndibaSession> findById(IndibaSessionId id) {
        Optional<IndibaJpaEntity> indibaJpaEntity = indibaJpaDataRepository.findById(id.value());
        return indibaJpaEntity.map(IndibaJpaMapper::toModelEntity);
    }

    @Override
    public PagedResponse<IndibaSummaryElement> findAllByPatientId(PageQuery query, PatientId patientId) {
        Pageable pageable = PageRequest.of(query.page(), query.size());
        Page<IndibaSummaryJpaProjection> page = indibaJpaDataRepository.findAllByPatientId(patientId.value(), pageable);

        List<IndibaSummaryElement> content = page.getContent().stream()
                .map(proj -> new IndibaSummaryElement(proj.id(), proj.beginSession()))
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
    public List<Object[]> countSessionGroupedByMonth(PatientId patientId, Integer year) {
        return indibaJpaDataRepository.countSessionByMonthForYear(patientId.value(), year);
    }

    @Override
    public List<IndibaSession> findAllForExport() {
        return indibaJpaDataRepository.findAll().stream()
                .map(IndibaJpaMapper::toModelEntity)
                .toList();
    }

    @Override
    public List<IndibaSession> findAllByPatientId(PatientId patientId) {
        return indibaJpaDataRepository.findByPatientId(patientId.value())
                .stream()
                .map(IndibaJpaMapper::toModelEntity)
                .toList();
    }
}
