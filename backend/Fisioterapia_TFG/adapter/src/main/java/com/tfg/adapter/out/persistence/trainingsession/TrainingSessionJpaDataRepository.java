package com.tfg.adapter.out.persistence.trainingsession;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TrainingSessionJpaDataRepository extends JpaRepository<TrainingSessionJpaEntity, Integer> {
    @Query("SELECT t.date FROM TrainingSessionJpaEntity t WHERE t.patient.id = ?1")
    List<LocalDate> findAllByPatientId(Integer patientId);

    @Query("SELECT EXTRACT(MONTH FROM t.date) as month, COUNT(t.id) as count FROM TrainingSessionJpaEntity t WHERE t.patient.id = ?1 AND EXTRACT(YEAR FROM t.date) = ?2 GROUP BY month")
    List<Object[]> countSessionsByMonthForYear(Integer patientId, Integer year);

    @Query("""
    SELECT 
        CAST(t.date AS DATE) as sessionDate, 
        SUM(s.reps * s.weightKg) as totalVolume
    FROM TrainingSessionJpaEntity t
    JOIN t.exercises e
    JOIN e.sets s
    WHERE t.patient.id = :patientId 
      AND e.name = :exerciseName 
    GROUP BY CAST(t.date AS DATE)
    ORDER BY CAST(t.date AS DATE) ASC
    """)
    List<Object[]> calculateVolumeProgression(
            @Param("patientId") Integer patientId,
            @Param("exerciseName") String exerciseName
    );
}
