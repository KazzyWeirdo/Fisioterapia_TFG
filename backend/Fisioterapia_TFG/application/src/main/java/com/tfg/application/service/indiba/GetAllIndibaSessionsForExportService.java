package com.tfg.application.service.indiba;

import com.tfg.model.indiba.IndibaSession;
import com.tfg.application.port.in.indiba.GetAllIndibaSessionsForExportUseCase;
import com.tfg.application.port.out.persistence.IndibaSessionRepository;

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
