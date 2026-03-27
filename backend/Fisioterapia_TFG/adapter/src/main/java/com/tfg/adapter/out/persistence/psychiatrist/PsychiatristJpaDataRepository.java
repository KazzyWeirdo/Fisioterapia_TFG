package com.tfg.adapter.out.persistence.psychiatrist;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PsychiatristJpaDataRepository extends JpaRepository<PsychiatristJpaEntity, Integer> {
    Optional<PsychiatristJpaEntity> findByEmail(String email);
}
