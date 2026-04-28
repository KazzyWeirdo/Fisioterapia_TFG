package com.tfg.service.indiba;

import com.tfg.indiba.IndibaSession;
import com.tfg.port.in.indiba.GetAllIndibaSessionsForExportUseCase;
import com.tfg.port.out.persistence.IndibaSessionRepository;

import java.util.List;

public class GetAllIndibaSessionsForExportService implements GetAllIndibaSessionsForExportUseCase {

    private final IndibaSessionRepository indibaSessionRepository;

    public GetAllIndibaSessionsForExportService(IndibaSessionRepository indibaSessionRepository) {
        this.indibaSessionRepository = indibaSessionRepository;
    }

    @Override
    public List<IndibaSession> getAllIndibaSessionsForExport() {
        return indibaSessionRepository.findAllForExport();
    }
}
