package com.tfg.application.service.pni;

import com.tfg.application.exceptions.InvalidIdException;
import com.tfg.model.pni.PniReport;
import com.tfg.model.pni.PniReportId;
import com.tfg.application.port.in.pni.GetPniReportUseCase;
import com.tfg.application.port.out.persistence.PniReportRepository;

public class GetPniReportService implements GetPniReportUseCase {

    private final PniReportRepository pniReportRepository;

    public GetPniReportService(PniReportRepository pniReportRepository) {
        this.pniReportRepository = pniReportRepository;
    }

    @Override
    public PniReport getPniReport(PniReportId id) {
        return pniReportRepository.findById(id)
                .orElseThrow(InvalidIdException::new);
    }
}
