package com.tfg.service.psychiatrist;

import com.tfg.port.in.psychiatrist.RegisterPsychiatristUseCase;
import com.tfg.port.out.persistence.PsychiatristRepository;
import com.tfg.psychiatrist.Psychiatrist;

public class RegisterPsychiatristService implements RegisterPsychiatristUseCase {
    private final PsychiatristRepository psychiatristRepository;

    public RegisterPsychiatristService(PsychiatristRepository psychiatristRepository) {
        this.psychiatristRepository = psychiatristRepository;
    }

    @Override
    public void registerPsychiatrist(Psychiatrist psychiatrist) {
        if (psychiatristRepository.findByEmail(psychiatrist.getEmail()).isPresent()) {
            throw new IllegalArgumentException("A psychiatrist with this email already exists.");
        }

        psychiatristRepository.save(psychiatrist);
    }
}
