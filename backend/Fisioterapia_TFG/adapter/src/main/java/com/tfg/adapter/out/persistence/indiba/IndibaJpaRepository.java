package com.tfg.adapter.out.persistence.indiba;

import com.tfg.adapter.out.persistence.patient.PatientJpaEntity;
import com.tfg.adapter.out.persistence.patient.PatientJpaMapper;
import com.tfg.adapter.out.persistence.physiotherapist.PhysiotherapistJpaEntity;
import com.tfg.adapter.out.persistence.physiotherapist.PhysiotherapistJpaMapper;
import com.tfg.indiba.IndibaSession;
import com.tfg.indiba.IndibaSessionId;
import com.tfg.patient.PatientId;
import com.tfg.port.out.persistence.IndibaSessionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.Date;
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
    public Optional<IndibaSession> findById(IndibaSessionId id) {
        Optional<IndibaJpaEntity> indibaJpaEntity = indibaJpaDataRepository.findById(id.value());
        return indibaJpaEntity.map(IndibaJpaMapper::toModelEntity);
    }

    @Override
    public List<IndibaSession> findAllByPatientId(PatientId patientId) {
        return indibaJpaDataRepository.findAllByPatientId(patientId.value()).stream()
                .map(IndibaJpaMapper::toModelEntity)
                .toList();
    }

    @Override
    public List<Object[]> countSessionGroupedByMonth(PatientId patientId, Integer year) {
        return indibaJpaDataRepository.countSessionByMonthForYear(patientId.value(), year);
    }
}
