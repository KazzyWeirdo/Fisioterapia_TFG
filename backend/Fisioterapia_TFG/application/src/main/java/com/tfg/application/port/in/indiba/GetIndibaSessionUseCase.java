package com.tfg.application.port.in.indiba;

import com.tfg.model.indiba.IndibaSession;
import com.tfg.model.indiba.IndibaSessionId;

public interface GetIndibaSessionUseCase {
    IndibaSession getIndibaSession(IndibaSessionId id);
}
