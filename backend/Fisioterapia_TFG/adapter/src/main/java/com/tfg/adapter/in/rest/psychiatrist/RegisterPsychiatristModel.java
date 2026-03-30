package com.tfg.adapter.in.rest.psychiatrist;

import com.tfg.psychiatrist.ERole;
import com.tfg.psychiatrist.Psychiatrist;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public record RegisterPsychiatristModel(
        @NotNull(message = "Email is required")
        String email,
        @NotNull(message = "Password is required")
        String password,
        @NotNull(message = "Name is required")
        String name,
        @NotNull(message = "Surname is required")
        String surname,
        @NotEmpty(message = "The user must have at least a role")
        List<String> role
) {
    public Psychiatrist toDomainModel() {
        return new Psychiatrist(email, password, name, surname, role.stream().map(ERole::valueOf).collect(Collectors.toSet()));
    }
}
