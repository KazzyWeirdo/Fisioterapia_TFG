package com.tfg.adapter.in.rest.pni;

import com.tfg.pni.PniReport;
import com.tfg.port.in.pni.GetAllPniReportsForExportUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/pni")
public class GetAllPniReportsForExportController {

    private final GetAllPniReportsForExportUseCase getAllPniReportsForExportUseCase;

    public GetAllPniReportsForExportController(GetAllPniReportsForExportUseCase getAllPniReportsForExportUseCase) {
        this.getAllPniReportsForExportUseCase = getAllPniReportsForExportUseCase;
    }

    @GetMapping("/export")
    public ResponseEntity<List<PniExportWebModel>> exportPniReports() {
        List<PniReport> reports = getAllPniReportsForExportUseCase.getAllPniReportsForExport();
        if (reports.isEmpty()) return ResponseEntity.noContent().build();
        List<PniExportWebModel> dto = reports.stream().map(r -> new PniExportWebModel(
                r.getPatient().getId().value(),
                r.getId().value(),
                r.getReportDate().toString(),
                r.getHours_asleep(),
                r.getHrv(),
                r.getAns_charge(),
                r.getSleep_score()
        )).toList();
        return ResponseEntity.ok(dto);
    }
}
