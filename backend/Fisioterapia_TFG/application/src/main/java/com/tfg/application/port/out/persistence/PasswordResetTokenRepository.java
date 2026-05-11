package com.tfg.application.port.out.persistence;

import com.tfg.model.passwordresettoken.PasswordResetToken;

import java.util.Optional;

public interface PasswordResetTokenRepository {
    void save(PasswordResetToken token);
    Optional<PasswordResetToken> findByToken(String token);
    void delete(String token);
}
