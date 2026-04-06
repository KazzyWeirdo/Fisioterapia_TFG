package com.tfg.adapter.out.persistence.physiotherapist;

import com.tfg.physiotherapist.ERole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "roles")
public class RoleJpaEntity {
    @Id
    private int id;
    @Enumerated(EnumType.STRING)
    private ERole name;
}