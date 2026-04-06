package com.tfg.adapter.in.rest.physiotherapist;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AuthenticationRequest (@NotNull Integer physioId, @NotBlank String password) {
}
