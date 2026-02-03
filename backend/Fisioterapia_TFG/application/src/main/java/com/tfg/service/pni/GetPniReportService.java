package com.tfg.service.pni;

import com.tfg.exceptions.InvalidIdException;
import com.tfg.pni.PniReport;
import com.tfg.pni.PniReportId;
import com.tfg.port.in.pni.GetPniReportUseCase;
import com.tfg.port.out.persistence.PniReportRepository;

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
