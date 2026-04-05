package com.tfg.adapter.out.persistence.physiotherapist;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhysiotherapistJpaDataRepository extends JpaRepository<PhysiotherapistJpaEntity, Integer> {
    Optional<PhysiotherapistJpaEntity> findByEmail(String email);
}
