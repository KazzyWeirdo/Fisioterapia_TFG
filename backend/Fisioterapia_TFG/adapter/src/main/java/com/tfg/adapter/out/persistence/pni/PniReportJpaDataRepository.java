package com.tfg.adapter.out.persistence.pni;

import com.tfg.adapter.out.persistence.indiba.IndibaSummaryJpaProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PniReportJpaDataRepository extends JpaRepository <PniReportJpaEntity, Integer> {

    @Query("""
        SELECT new com.tfg.adapter.out.persistence.pni.PniReportSummaryJpaProjection(
            s.id,
            s.reportDate
        )
        FROM PniReportJpaEntity s
        WHERE s.patient.id = :patientId
    """)
    Page<PniReportSummaryJpaProjection> findAllByPatientId(
            @Param("patientId") int patientId,
            Pageable pageable
    );
}
