package com.tfg.adapter.out.persistence.indiba;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface IndibaJpaDataRepository extends JpaRepository<IndibaJpaEntity, Integer> {

    @Query("SELECT i.beginSession FROM IndibaJpaEntity i WHERE i.patient.id = ?1")
    List<Date>findAllByPatientId(Integer patientId);
}
