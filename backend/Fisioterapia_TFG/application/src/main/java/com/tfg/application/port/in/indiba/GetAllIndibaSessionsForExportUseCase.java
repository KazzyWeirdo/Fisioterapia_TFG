package com.tfg.application.port.in.indiba;

import com.tfg.model.indiba.IndibaSession;

import java.util.List;

public interface GetAllIndibaSessionsForExportUseCase {
    List<IndibaSession> getAllIndibaSessionsForExport();
}
