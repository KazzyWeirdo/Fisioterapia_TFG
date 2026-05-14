package com.tfg.service.pni;

import com.tfg.pni.PniReport;
import com.tfg.port.in.pni.CreatePniReportUseCase;
import com.tfg.port.out.persistence.PniReportRepository;

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
