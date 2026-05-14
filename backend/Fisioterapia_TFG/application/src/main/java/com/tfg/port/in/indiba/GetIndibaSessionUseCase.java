package com.tfg.port.in.indiba;

import com.tfg.indiba.IndibaSession;
import com.tfg.indiba.IndibaSessionId;

public interface GetIndibaSessionUseCase {
    IndibaSession getIndibaSession(IndibaSessionId id);
}
