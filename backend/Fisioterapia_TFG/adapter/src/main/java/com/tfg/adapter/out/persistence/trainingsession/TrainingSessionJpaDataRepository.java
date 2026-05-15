package com.tfg.adapter.out.persistence.trainingsession;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TrainingSessionJpaDataRepository extends JpaRepository<TrainingSessionJpaEntity, Integer> {
    @Query("""
        SELECT new com.tfg.adapter.out.persistence.trainingsession.TrainingSessionSummaryJpaProjection(
            s.id,
            s.startDateTime,
            s.endDateTime,
            s.physiotherapist.name,
            s.physiotherapist.surname,
            MIN(et.name)
        )
        FROM TrainingSessionJpaEntity s
        LEFT JOIN s.exerciseTemplates et
        WHERE s.patient.id = :patientId
        GROUP BY s.id, s.startDateTime, s.endDateTime, s.physiotherapist.name, s.physiotherapist.surname
        ORDER BY s.startDateTime ASC, s.id ASC
    """)
    Page<TrainingSessionSummaryJpaProjection> findAllByPatientId(
            @Param("patientId") int patientId,
            Pageable pageable
    );

    @Query("SELECT EXTRACT(MONTH FROM t.startDateTime) as month, COUNT(t.id) as count FROM TrainingSessionJpaEntity t WHERE t.patient.id = ?1 AND EXTRACT(YEAR FROM t.startDateTime) = ?2 GROUP BY month")
    List<Object[]> countSessionsByMonthForYear(Integer patientId, Integer year);

    @Query("""
    SELECT
        CAST(t.startDateTime AS DATE) as sessionDate,
        AVG(s.rpe) as averageRpe
    FROM TrainingSessionJpaEntity t
    JOIN t.exerciseTemplates et
    JOIN et.exercises e
    JOIN e.sets s
    WHERE t.patient.id = :patientId
      AND LOWER(e.name) = LOWER(:exerciseName)
    GROUP BY CAST(t.startDateTime AS DATE)
    ORDER BY CAST(t.startDateTime AS DATE) ASC
    """)
    List<Object[]> calculateRpeProgression(
            @Param("patientId") Integer patientId,
            @Param("exerciseName") String exerciseName
    );

    void deleteByPatientId(int patientId);
}
