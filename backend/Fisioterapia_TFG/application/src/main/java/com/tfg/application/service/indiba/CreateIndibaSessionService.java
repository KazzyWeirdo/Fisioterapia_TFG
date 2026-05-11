package com.tfg.application.service.indiba;

import com.tfg.model.indiba.IndibaSession;
import com.tfg.application.port.in.indiba.CreateIndibaSessionUseCase;
import com.tfg.application.port.out.persistence.IndibaSessionRepository;

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
