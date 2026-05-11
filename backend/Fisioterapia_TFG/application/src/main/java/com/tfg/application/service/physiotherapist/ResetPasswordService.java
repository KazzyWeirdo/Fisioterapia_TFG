package com.tfg.application.service.physiotherapist;

import com.tfg.application.exceptions.InvalidTokenException;
import com.tfg.model.passwordresettoken.PasswordResetToken;
import com.tfg.application.port.in.physiotherapist.ResetPasswordUseCase;
import com.tfg.application.port.out.persistence.PasswordResetTokenRepository;
import com.tfg.application.port.out.persistence.PhysiotherapistRepository;
import com.tfg.application.port.out.springsecurity.PasswordEncoderPort;

public class ResetPasswordService implements ResetPasswordUseCase {

    private final PasswordResetTokenRepository tokenRepository;
    private final PhysiotherapistRepository physiotherapistRepository;
    private final PasswordEncoderPort passwordEncoderPort;

    public ResetPasswordService(PasswordResetTokenRepository tokenRepository, PhysiotherapistRepository physiotherapistRepository,
                                PasswordEncoderPort passwordEncoderPort) {
        this.tokenRepository = tokenRepository;
        this.physiotherapistRepository = physiotherapistRepository;
        this.passwordEncoderPort = passwordEncoderPort;
    }

    @Override
    public void reset(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Invalid or non-existent token."));

        if (resetToken.isExpired()) {
            tokenRepository.delete(token);
            throw new InvalidTokenException("Token has expired.");
        }

        String encodedPassword = passwordEncoderPort.encode(newPassword);
        physiotherapistRepository.updatePassword(resetToken.physio().getId(), encodedPassword);
        tokenRepository.delete(token);
    }
}