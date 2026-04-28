package com.tfg.adapter.out.persistence.trainingsession;

import com.tfg.adapter.out.persistence.patient.PatientJpaEntity;
import com.tfg.adapter.out.persistence.patient.PatientJpaMapper;
import com.tfg.patient.PatientId;
import com.tfg.pojos.pagedpojos.PageQuery;
import com.tfg.pojos.pagedpojos.PagedResponse;
import com.tfg.pojos.query.TrainingSessionSummaryElement;
import com.tfg.port.out.persistence.TrainingSessionRepository;
import com.tfg.trainingsession.TrainingSession;
import com.tfg.trainingsession.TrainingSessionId;
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
        TrainingSessionJpaEntity trainingSessionJpaEntity = TrainingSessionJpaMapper.toJpaEntity(trainingSession, patientJpaEntity);
        trainingSessionJpaDataRepository.save(trainingSessionJpaEntity);
    }

    @Override
    public void deleteAll() {
        trainingSessionJpaDataRepository.deleteAll();
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
                .map(proj -> new TrainingSessionSummaryElement(proj.id(), proj.date()))
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
    public List<Object[]> calculateVolumeProgression(PatientId patientId, String exerciseName) {
        return trainingSessionJpaDataRepository.calculateVolumeProgression(patientId.value(), exerciseName);
    }

    @Transactional(readOnly = true)
    @Override
    public List<TrainingSession> findAllForExport() {
        return trainingSessionJpaDataRepository.findAll().stream()
                .map(TrainingSessionJpaMapper::toModelEntity)
                .toList();
    }
}
