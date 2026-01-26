package com.tfg.adapter.out.persistence.patient;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientJpaDataRepository extends JpaRepository<PatientJpaEntity, Integer> {
    Optional<PatientJpaEntity> findByEmail(String email);
    Optional<PatientJpaEntity> findByDni(String dni);
}
