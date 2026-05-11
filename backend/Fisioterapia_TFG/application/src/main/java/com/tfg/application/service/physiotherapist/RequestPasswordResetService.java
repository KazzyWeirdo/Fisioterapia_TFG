package com.tfg.application.service.physiotherapist;

import com.tfg.model.passwordresettoken.PasswordResetToken;
import com.tfg.model.physiotherapist.PhysiotherapistEmail;
import com.tfg.application.port.in.physiotherapist.RequestPasswordResetUseCase;
import com.tfg.application.port.out.mail.EmailSenderPort;
import com.tfg.application.port.out.persistence.PasswordResetTokenRepository;
import com.tfg.application.port.out.persistence.PhysiotherapistRepository;

import java.time.LocalDateTime;
import java.util.UUID;

public class RequestPasswordResetService implements RequestPasswordResetUseCase {

    private final PhysiotherapistRepository physiotherapistRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final EmailSenderPort emailSenderPort;

    public RequestPasswordResetService(PhysiotherapistRepository physiotherapistRepository,
                                      PasswordResetTokenRepository tokenRepository,
                                      EmailSenderPort emailSenderPort) {
        this.physiotherapistRepository = physiotherapistRepository;
        this.tokenRepository = tokenRepository;
        this.emailSenderPort = emailSenderPort;
    }

    @Override
    public void requestReset(String email) {
        physiotherapistRepository.findByEmail(new PhysiotherapistEmail(email)).ifPresent(physio -> {
            String token = UUID.randomUUID().toString();
            LocalDateTime expiresAt = LocalDateTime.now().plusHours(1);

            tokenRepository.save(new PasswordResetToken(token, physio, expiresAt));

            emailSenderPort.sendPasswordResetLink(email, token);
        });
    }
}
