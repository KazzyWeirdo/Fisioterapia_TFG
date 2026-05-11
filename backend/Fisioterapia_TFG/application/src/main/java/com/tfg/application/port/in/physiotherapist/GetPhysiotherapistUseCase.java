package com.tfg.application.port.in.physiotherapist;

import com.tfg.model.physiotherapist.Physiotherapist;
import com.tfg.model.physiotherapist.PhysiotherapistId;

public interface GetPhysiotherapistUseCase {
    Physiotherapist getPhysiotherapist(PhysiotherapistId id);
}
