package com.tfg.adapter.out.persistence.trainingsession;

import com.tfg.adapter.out.persistence.patient.PatientJpaEntity;
import com.tfg.adapter.out.persistence.patient.PatientJpaMapper;
import com.tfg.adapter.out.persistence.physiotherapist.PhysiotherapistJpaEntity;
import com.tfg.adapter.out.persistence.physiotherapist.PhysiotherapistJpaMapper;
import com.tfg.model.patient.PatientId;
import com.tfg.application.pojos.pagedpojos.PageQuery;
import com.tfg.application.pojos.pagedpojos.PagedResponse;
import com.tfg.application.pojos.query.TrainingSessionSummaryElement;
import com.tfg.application.port.out.persistence.TrainingSessionRepository;
import com.tfg.model.trainingsession.TrainingSession;
import com.tfg.model.trainingsession.TrainingSessionId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class TrainingSessionJpaRepository implements TrainingSessionRepository {
    private final TrainingSessionJpaDataRepository trainingSessionJpaDataRepository;

    public TrainingSessionJpaRepository(TrainingSessionJpaDataRepository trainingSessionJpaDataRepository) {
        this.trainingSessionJpaDataRepository = trainingSessionJpaDataRepository;
    }

    @Override
    @Transactional
    public void save(TrainingSession trainingSession) {
        PatientJpaEntity patientJpaEntity = PatientJpaMapper.toJpaEntity(trainingSession.getPatient());
        PhysiotherapistJpaEntity physiotherapistJpaEntity = PhysiotherapistJpaMapper.toJpaEntity(trainingSession.getPhysiotherapist());
        TrainingSessionJpaEntity trainingSessionJpaEntity = TrainingSessionJpaMapper.toJpaEntity(trainingSession, patientJpaEntity, physiotherapistJpaEntity);
        trainingSessionJpaDataRepository.save(trainingSessionJpaEntity);
    }

    @Override
    public void deleteAll() {
        trainingSessionJpaDataRepository.deleteAll();
    }

    @Override
    @Transactional
    public void deleteAllByPatientId(PatientId patientId) {
        trainingSessionJpaDataRepository.deleteByPatientId(patientId.value());
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<TrainingSession> findById(TrainingSessionId id) {
        return trainingSessionJpaDataRepository.findById(id.value())
                .map(TrainingSessionJpaMapper::toModelEntity);
    }

    @Override
    public PagedResponse<TrainingSessionSummaryElement> findAllByPatientId(PageQuery query, PatientId patientId) {
        Pageable pageable = PageRequest.of(query.page(), query.size());
        Page<TrainingSessionSummaryJpaProjection> page = trainingSessionJpaDataRepository.findAllByPatientId(patientId.value(), pageable);

        List<TrainingSessionSummaryElement> content = page.getContent().stream()
                .map(proj -> new TrainingSessionSummaryElement(
                        proj.id(),
                        proj.startDateTime(),
                        proj.endDateTime(),
                        proj.physiotherapistFirstName() + " " + proj.physiotherapistSurname(),
                        proj.templateName()
                ))
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
    public List<Object[]> countSessionByMonth(PatientId patientId, Integer year) {
        return trainingSessionJpaDataRepository.countSessionsByMonthForYear(patientId.value(), year);
    }

    @Override
    public List<Object[]> calculateRpeProgression(PatientId patientId, String exerciseName) {
        return trainingSessionJpaDataRepository.calculateRpeProgression(patientId.value(), exerciseName);
    }

    @Transactional(readOnly = true)
    @Override
    public List<TrainingSession> findAllForExport() {
        return trainingSessionJpaDataRepository.findAll().stream()
                .map(TrainingSessionJpaMapper::toModelEntity)
                .toList();
    }
}
