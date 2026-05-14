package com.tfg.port.in.physiotherapist;

import com.tfg.physiotherapist.Physiotherapist;
import com.tfg.physiotherapist.PhysiotherapistId;

public interface GetPhysiotherapistUseCase {
    Physiotherapist getPhysiotherapist(PhysiotherapistId id);
}
