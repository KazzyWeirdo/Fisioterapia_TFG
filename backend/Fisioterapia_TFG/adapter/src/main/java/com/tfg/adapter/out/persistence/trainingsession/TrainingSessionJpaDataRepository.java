package com.tfg.adapter.out.persistence.trainingsession;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface TrainingSessionJpaDataRepository extends JpaRepository<TrainingSessionJpaEntity, Integer> {
    @Query("SELECT t.date FROM TrainingSessionJpaEntity t WHERE t.patient.id = ?1")
    List<LocalDate> findAllByPatientId(Integer patientId);
}
