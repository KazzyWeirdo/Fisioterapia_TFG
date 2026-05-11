package com.tfg.application.service.physiotherapist;

import com.tfg.application.exceptions.InvalidIdException;
import com.tfg.model.physiotherapist.Physiotherapist;
import com.tfg.model.physiotherapist.PhysiotherapistId;
import com.tfg.application.port.in.physiotherapist.GetPhysiotherapistUseCase;
import com.tfg.application.port.out.persistence.PhysiotherapistRepository;

public class GetPhysiotherapistService implements GetPhysiotherapistUseCase {

    private final PhysiotherapistRepository physiotherapistRepository;

    public GetPhysiotherapistService(PhysiotherapistRepository physiotherapistRepository) {
        this.physiotherapistRepository = physiotherapistRepository;
    }

    @Override
    public Physiotherapist getPhysiotherapist(PhysiotherapistId id) {
        return physiotherapistRepository.findById(id)
                .orElseThrow(InvalidIdException::new);
    }
}
