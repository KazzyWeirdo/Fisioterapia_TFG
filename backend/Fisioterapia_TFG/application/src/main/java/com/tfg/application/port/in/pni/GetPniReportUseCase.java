package com.tfg.application.port.in.pni;

import com.tfg.model.pni.PniReport;
import com.tfg.model.pni.PniReportId;

public interface GetPniReportUseCase {
    PniReport getPniReport(PniReportId id);
}
