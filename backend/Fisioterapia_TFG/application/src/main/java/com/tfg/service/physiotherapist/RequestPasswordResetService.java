package com.tfg.service.physiotherapist;

import com.tfg.passwordresettoken.PasswordResetToken;
import com.tfg.physiotherapist.PhysiotherapistEmail;
import com.tfg.port.in.physiotherapist.RequestPasswordResetUseCase;
import com.tfg.port.out.mail.EmailSenderPort;
import com.tfg.port.out.persistence.PasswordResetTokenRepository;
import com.tfg.port.out.persistence.PhysiotherapistRepository;

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
