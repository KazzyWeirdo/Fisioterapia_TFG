package com.tfg.service.physiotherapist;

import com.tfg.exceptions.InvalidIdException;
import com.tfg.physiotherapist.Physiotherapist;
import com.tfg.physiotherapist.PhysiotherapistId;
import com.tfg.port.in.physiotherapist.GetPhysiotherapistUseCase;
import com.tfg.port.out.persistence.PhysiotherapistRepository;

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
