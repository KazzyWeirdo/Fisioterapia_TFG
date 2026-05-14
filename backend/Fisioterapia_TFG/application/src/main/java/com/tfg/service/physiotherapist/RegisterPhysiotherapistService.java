package com.tfg.service.physiotherapist;

import com.tfg.port.in.physiotherapist.RegisterPhysiotherapistUseCase;
import com.tfg.port.in.physiotherapist.RequestPasswordResetUseCase;
import com.tfg.port.out.persistence.PhysiotherapistRepository;
import com.tfg.physiotherapist.Physiotherapist;
import com.tfg.port.out.springsecurity.PasswordEncoderPort;

import java.util.UUID;

public class RegisterPhysiotherapistService implements RegisterPhysiotherapistUseCase {
    private final PhysiotherapistRepository repository;
    private final PasswordEncoderPort passwordEncoderPort;
    private final RequestPasswordResetUseCase requestPasswordResetUseCase;

    public RegisterPhysiotherapistService(PhysiotherapistRepository repository,
                                          PasswordEncoderPort passwordEncoderPort,
                                          RequestPasswordResetUseCase requestPasswordResetUseCase) {
        this.repository = repository;
        this.passwordEncoderPort = passwordEncoderPort;
        this.requestPasswordResetUseCase = requestPasswordResetUseCase;
    }

    @Override
    public void registerPsychiatrist(Physiotherapist physiotherapist) {
        if (repository.findByEmail(physiotherapist.getEmail()).isPresent()) {
            throw new IllegalArgumentException("A physiotherapist with this email already exists.");
        }
        String randomPassword = UUID.randomUUID().toString() + "Aa1!";
        physiotherapist.setPassword(passwordEncoderPort.encode(randomPassword));
        repository.save(physiotherapist);
        requestPasswordResetUseCase.requestReset(physiotherapist.getEmail().value());
    }
}
