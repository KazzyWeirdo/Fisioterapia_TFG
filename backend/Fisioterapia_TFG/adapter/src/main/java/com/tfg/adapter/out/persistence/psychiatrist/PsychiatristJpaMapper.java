package com.tfg.adapter.out.persistence.psychiatrist;

import com.tfg.psychiatrist.ERole;

import java.util.Set;

public class PsychiatristJpaMapper {

    public static PsychiatristJpaEntity toJpaEntity(com.tfg.psychiatrist.Psychiatrist psychiatrist) {
        PsychiatristJpaEntity entity = new PsychiatristJpaEntity();
        entity.setId(psychiatrist.getId().value());
        entity.setName(psychiatrist.getName());
        entity.setSurname(psychiatrist.getSurname());
        entity.setEmail(psychiatrist.getEmail().value());
        entity.setPassword(psychiatrist.getPassword());
        return entity;
    }

    public static com.tfg.psychiatrist.Psychiatrist toModelEntity(PsychiatristJpaEntity entity) {
        Set<ERole> roles = entity.getRoles().stream()
                .map(RoleJpaEntity::getName)
                .collect(java.util.stream.Collectors.toSet());
        return new com.tfg.psychiatrist.Psychiatrist(
                entity.getEmail(),
                entity.getPassword(),
                entity.getName(),
                entity.getSurname(),
                roles
        );
    }
}
