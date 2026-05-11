package com.tfg.application.service.pni;

import com.tfg.model.pni.PniReport;
import com.tfg.application.port.in.pni.GetAllPniReportsForExportUseCase;
import com.tfg.application.port.out.persistence.PniReportRepository;

import java.util.List;

public class GetAllPniReportsForExportService implements GetAllPniReportsForExportUseCase {

    private final PniReportRepository pniReportRepository;

    public GetAllPniReportsForExportService(PniReportRepository pniReportRepository) {
        this.pniReportRepository = pniReportRepository;
    }

    @Override
    public List<PniReport> getAllPniReportsForExport() {
        return pniReportRepository.findAllForExport();
    }
}
