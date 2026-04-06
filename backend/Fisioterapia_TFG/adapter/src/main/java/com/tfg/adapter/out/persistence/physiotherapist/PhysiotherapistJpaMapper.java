package com.tfg.adapter.out.persistence.physiotherapist;

import com.tfg.physiotherapist.ERole;
import com.tfg.physiotherapist.Physiotherapist;

import java.util.Set;

public class PhysiotherapistJpaMapper {

    public static PhysiotherapistJpaEntity toJpaEntity(Physiotherapist psychiatrist) {
        PhysiotherapistJpaEntity entity = new PhysiotherapistJpaEntity();
        entity.setId(psychiatrist.getId().value());
        entity.setName(psychiatrist.getName());
        entity.setSurname(psychiatrist.getSurname());
        entity.setEmail(psychiatrist.getEmail().value());
        entity.setPassword(psychiatrist.getPassword());
        return entity;
    }

    public static Physiotherapist toModelEntity(PhysiotherapistJpaEntity entity) {
        Set<ERole> roles = entity.getRoles().stream()
                .map(RoleJpaEntity::getName)
                .collect(java.util.stream.Collectors.toSet());
        return new Physiotherapist(
                entity.getEmail(),
                entity.getPassword(),
                entity.getName(),
                entity.getSurname(),
                roles
        );
    }
}
