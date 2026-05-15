package com.tfg.application.service.pni;

import com.tfg.model.pni.PniReport;
import com.tfg.application.port.in.pni.CreatePniReportUseCase;
import com.tfg.application.port.out.persistence.PniReportRepository;

public class CreatePniReportService implements CreatePniReportUseCase {

    private final PniReportRepository pniReportRepository;

    public CreatePniReportService(PniReportRepository pniReportRepository) {
        this.pniReportRepository = pniReportRepository;
    }

    @Override
    public void createPniReport(PniReport pniReport) {
        pniReportRepository.save(pniReport);
    }
}
