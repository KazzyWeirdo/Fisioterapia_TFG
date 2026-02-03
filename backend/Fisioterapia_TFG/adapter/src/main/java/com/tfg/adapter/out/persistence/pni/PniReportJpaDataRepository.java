package com.tfg.adapter.out.persistence.pni;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface PniReportJpaDataRepository extends JpaRepository <PniReportJpaEntity, Integer> {

    @Query("SELECT p.reportDate FROM PniReportJpaEntity p WHERE p.patient.id = ?1")
    List<LocalDate> findAllReportDatesByPatientId(Integer patientId);
}
