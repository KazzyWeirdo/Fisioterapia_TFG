package com.tfg.service.indiba;

import com.tfg.exceptions.InvalidIdException;
import com.tfg.indiba.IndibaSession;
import com.tfg.indiba.IndibaSessionId;
import com.tfg.port.in.indiba.GetIndibaSessionUseCase;
import com.tfg.port.out.persistence.IndibaSessionRepository;

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
