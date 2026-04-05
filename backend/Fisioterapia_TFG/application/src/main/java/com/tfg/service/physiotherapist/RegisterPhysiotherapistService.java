package com.tfg.service.physiotherapist;

import com.tfg.port.in.physiotherapist.RegisterPhysiotherapistUseCase;
import com.tfg.port.out.persistence.PhysiotherapistRepository;
import com.tfg.physiotherapist.Physiotherapist;

public class RegisterPhysiotherapistService implements RegisterPhysiotherapistUseCase {
    private final PhysiotherapistRepository psychiatristRepository;

    public RegisterPhysiotherapistService(PhysiotherapistRepository psychiatristRepository) {
        this.psychiatristRepository = psychiatristRepository;
    }

    @Override
    public void registerPsychiatrist(Physiotherapist psychiatrist) {
        if (psychiatristRepository.findByEmail(psychiatrist.getEmail()).isPresent()) {
            throw new IllegalArgumentException("A psychiatrist with this email already exists.");
        }

        psychiatristRepository.save(psychiatrist);
    }
}
