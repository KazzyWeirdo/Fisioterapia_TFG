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
        @NotNull(message = "Password is required")
        @NotBlank(message = "Password cannot be blank")
        @Size(min = 12, message = "Password cannot be smaller than 12 characters")
        @Pattern(regexp = ".*[A-Z].*", message = "Password must contain at least one uppercase")
        @Pattern(regexp = ".*[a-z].*", message = "Password must contain at least one lowercase")
        @Pattern(regexp = ".*\\d.*", message = "Password must contain at least one digit")
        @Pattern(regexp = ".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*", message = "Password must contain at least one special character")
        String password,
        @NotNull(message = "Name is required")
        String name,
        @NotNull(message = "Surname is required")
        String surname,
        @NotEmpty(message = "The user must have at least a role")
        List<String> role
) {
    public Physiotherapist toDomainModel() {
        return new Physiotherapist(email, password, name, surname, role.stream().map(ERole::valueOf).collect(Collectors.toSet()));
    }
}
