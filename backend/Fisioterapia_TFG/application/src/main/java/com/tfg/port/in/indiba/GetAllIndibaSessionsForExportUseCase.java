package com.tfg.port.in.indiba;

import com.tfg.indiba.IndibaSession;

import java.util.List;

public interface GetAllIndibaSessionsForExportUseCase {
    List<IndibaSession> getAllIndibaSessionsForExport();
}
