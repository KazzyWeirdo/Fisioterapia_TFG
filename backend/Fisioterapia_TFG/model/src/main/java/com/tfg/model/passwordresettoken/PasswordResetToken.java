package com.tfg.model.passwordresettoken;

import com.tfg.model.physiotherapist.Physiotherapist;

import java.time.LocalDateTime;

public record PasswordResetToken(String token, Physiotherapist physio, LocalDateTime expiresAt) {
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }

}
