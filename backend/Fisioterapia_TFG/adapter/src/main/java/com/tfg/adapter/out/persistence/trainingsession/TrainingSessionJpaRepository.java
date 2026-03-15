package com.tfg.adapter.out.persistence.trainingsession;

import com.tfg.adapter.out.persistence.patient.PatientJpaEntity;
import com.tfg.adapter.out.persistence.patient.PatientJpaMapper;
import com.tfg.patient.PatientId;
import com.tfg.port.out.persistence.TrainingSessionRepository;
import com.tfg.trainingsession.Exercise;
import com.tfg.trainingsession.ExerciseSet;
import com.tfg.trainingsession.TrainingSession;
import com.tfg.trainingsession.TrainingSessionId;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class TrainingSessionJpaRepository implements TrainingSessionRepository {
    private final TrainingSessionJpaDataRepository trainingSessionJpaDataRepository;

    public TrainingSessionJpaRepository(TrainingSessionJpaDataRepository trainingSessionJpaDataRepository) {
        this.trainingSessionJpaDataRepository = trainingSessionJpaDataRepository;
    }

    @Override
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
    public List<LocalDate> findAllByPatientId(PatientId patientId) {
        return trainingSessionJpaDataRepository.findAllByPatientId(patientId.value());
    }

    @Override
    public List<Object[]> countSessionByMonth(PatientId patientId, Integer year) {
        return trainingSessionJpaDataRepository.countSessionsByMonthForYear(patientId.value(), year);
    }
}
