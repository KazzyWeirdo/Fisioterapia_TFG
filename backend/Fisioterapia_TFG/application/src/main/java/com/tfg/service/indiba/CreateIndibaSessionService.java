package com.tfg.service.indiba;

import com.tfg.indiba.IndibaSession;
import com.tfg.port.in.indiba.CreateIndibaSessionUseCase;
import com.tfg.port.out.persistence.IndibaSessionRepository;

public class CreateIndibaSessionService implements CreateIndibaSessionUseCase {

    private final IndibaSessionRepository indibaSessionRepository;

    public CreateIndibaSessionService(IndibaSessionRepository indibaSessionRepository) {
        this.indibaSessionRepository = indibaSessionRepository;
    }

    @Override
    public void createIndibaSession(IndibaSession indibaSession) {
        indibaSessionRepository.save(indibaSession);
    }
}
