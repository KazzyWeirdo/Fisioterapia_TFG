package com.tfg.port.in.pni;

import com.tfg.pni.PniReport;
import com.tfg.pni.PniReportId;

public interface GetPniReportUseCase {
    PniReport getPniReport(PniReportId id);
}
