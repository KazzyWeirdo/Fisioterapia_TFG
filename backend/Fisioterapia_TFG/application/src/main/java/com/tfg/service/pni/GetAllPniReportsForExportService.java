package com.tfg.service.pni;

import com.tfg.pni.PniReport;
import com.tfg.port.in.pni.GetAllPniReportsForExportUseCase;
import com.tfg.port.out.persistence.PniReportRepository;

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
