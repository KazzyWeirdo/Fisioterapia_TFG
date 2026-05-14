package com.tfg.adapter.out.persistence.physiotherapist;

import com.tfg.physiotherapist.ERole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleJpaDataRepository extends JpaRepository<RoleJpaEntity, Integer> {
    Optional<RoleJpaEntity> findByName(ERole name);
}
