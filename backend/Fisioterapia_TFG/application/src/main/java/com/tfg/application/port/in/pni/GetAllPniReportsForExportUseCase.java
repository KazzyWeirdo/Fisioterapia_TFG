package com.tfg.application.port.in.pni;

import com.tfg.model.pni.PniReport;

import java.util.List;

public interface GetAllPniReportsForExportUseCase {
    List<PniReport> getAllPniReportsForExport();
}
