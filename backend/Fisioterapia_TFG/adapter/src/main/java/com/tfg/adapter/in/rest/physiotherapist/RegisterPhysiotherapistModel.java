package com.tfg.adapter.in.rest.physiotherapist;

import com.tfg.physiotherapist.ERole;
import com.tfg.physiotherapist.Physiotherapist;
import jakarta.validation.constraints.*;

import java.util.List;
import java.util.stream.Collectors;

public record RegisterPhysiotherapistModel(
        @NotNull(message = "Email is required")
        @Email
        String email,
        @NotNull(message = "Name is required")
        String name,
        @NotNull(message = "Surname is required")
        String surname,
        String secondSurname,
        @NotEmpty(message = "The user must have at least a role")
        List<String> role
) {
    public Physiotherapist toDomainModel() {
        return new Physiotherapist(email, null, name, surname, role.stream().map(ERole::valueOf).collect(Collectors.toSet()));
    }
}
