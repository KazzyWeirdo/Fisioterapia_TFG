package com.tfg.application.service.indiba;

import com.tfg.application.exceptions.InvalidIdException;
import com.tfg.model.indiba.IndibaSession;
import com.tfg.model.indiba.IndibaSessionId;
import com.tfg.application.port.in.indiba.GetIndibaSessionUseCase;
import com.tfg.application.port.out.persistence.IndibaSessionRepository;

public class GetIndibaSessionService implements GetIndibaSessionUseCase {

    private final IndibaSessionRepository indibaSessionRepository;

    public GetIndibaSessionService(IndibaSessionRepository indibaSessionRepository) {
        this.indibaSessionRepository = indibaSessionRepository;
    }

    @Override
    public IndibaSession getIndibaSession(IndibaSessionId id) {
        return indibaSessionRepository.findById(id)
                .orElseThrow(InvalidIdException::new);
    }
}
