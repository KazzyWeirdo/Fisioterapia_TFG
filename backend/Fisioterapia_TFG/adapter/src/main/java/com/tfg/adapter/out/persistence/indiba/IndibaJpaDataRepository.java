package com.tfg.adapter.out.persistence.indiba;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IndibaJpaDataRepository extends JpaRepository<IndibaJpaEntity, Integer> {

    @Query("""
        SELECT new com.tfg.adapter.out.persistence.indiba.IndibaSummaryJpaProjection(
            s.id,
            s.beginSession
        )
        FROM IndibaJpaEntity s
        WHERE s.patient.id = :patientId
    """)
    Page<IndibaSummaryJpaProjection> findAllByPatientId(
            @Param("patientId") int patientId,
            Pageable pageable
    );

    @Query("SELECT EXTRACT(MONTH FROM i.beginSession) as month, COUNT(i.id) as total FROM IndibaJpaEntity i WHERE i.patient.id = ?1 AND EXTRACT(YEAR FROM i.beginSession) = ?2 GROUP BY month")
    List<Object[]> countSessionByMonthForYear(Integer patientId, Integer year);
}
