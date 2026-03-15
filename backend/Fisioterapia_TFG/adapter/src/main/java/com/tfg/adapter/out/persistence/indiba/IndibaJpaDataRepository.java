package com.tfg.adapter.out.persistence.indiba;

import com.tfg.indiba.IndibaSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface IndibaJpaDataRepository extends JpaRepository<IndibaJpaEntity, Integer> {

    @Query("SELECT i.beginSession FROM IndibaJpaEntity i WHERE i.patient.id = ?1")
    List<Date>findAllByPatientId(Integer patientId);

    @Query("SELECT EXTRACT(MONTH FROM i.beginSession) as month, COUNT(i.id) as total FROM IndibaJpaEntity i WHERE i.patient.id = ?1 AND EXTRACT(YEAR FROM i.beginSession) = ?2 GROUP BY month")
    List<Object[]> countSessionByMonthForYear(Integer patientId, Integer year);
}
