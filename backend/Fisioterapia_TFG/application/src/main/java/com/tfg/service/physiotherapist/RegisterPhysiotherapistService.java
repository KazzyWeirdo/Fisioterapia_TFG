package com.tfg.service.physiotherapist;

import com.tfg.port.in.physiotherapist.RegisterPhysiotherapistUseCase;
import com.tfg.port.out.persistence.PhysiotherapistRepository;
import com.tfg.physiotherapist.Physiotherapist;
import com.tfg.port.out.springsecurity.PasswordEncoderPort;

public class RegisterPhysiotherapistService implements RegisterPhysiotherapistUseCase {
    private final PhysiotherapistRepository psychiatristRepository;
    private final PasswordEncoderPort passwordEncoderPort;

    public RegisterPhysiotherapistService(PhysiotherapistRepository psychiatristRepository,
                                           PasswordEncoderPort passwordEncoderPort) {
        this.psychiatristRepository = psychiatristRepository;
        this.passwordEncoderPort = passwordEncoderPort;
    }

    @Override
    public void registerPsychiatrist(Physiotherapist physiotherapist) {
        if (psychiatristRepository.findByEmail(physiotherapist.getEmail()).isPresent()) {
            throw new IllegalArgumentException("A psychiatrist with this email already exists.");
        }
        physiotherapist.setPassword(passwordEncoderPort.encode(physiotherapist.getPassword()));
        psychiatristRepository.save(physiotherapist);
    }
}
