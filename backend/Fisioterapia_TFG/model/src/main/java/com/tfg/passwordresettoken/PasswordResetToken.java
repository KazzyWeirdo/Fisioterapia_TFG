package com.tfg.passwordresettoken;

import com.tfg.physiotherapist.Physiotherapist;

import java.time.LocalDateTime;

public record PasswordResetToken(String token, Physiotherapist physio, LocalDateTime expiresAt) {
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }

}
