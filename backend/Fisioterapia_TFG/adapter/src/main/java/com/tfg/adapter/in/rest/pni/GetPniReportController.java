package com.tfg.adapter.in.rest.pni;

import com.tfg.adapter.in.rest.common.PniReportIdParser;
import com.tfg.pni.PniReport;
import com.tfg.pni.PniReportId;
import com.tfg.port.in.pni.GetPniReportUseCase;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pni")
public class GetPniReportController {

    private final GetPniReportUseCase getPniReportUseCase;

    public GetPniReportController(GetPniReportUseCase getPniReportUseCase) {
        this.getPniReportUseCase = getPniReportUseCase;
    }

    @GetMapping("/{reportId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pni report found successfully"),
            @ApiResponse(responseCode = "404", description = "Pni report not found")
    })
    public ResponseEntity<PniReportWebModel> getPniReport(@PathVariable("reportId") String reportId) {
        PniReportId pniReportId = PniReportIdParser.parsePniReportId(reportId);
        PniReport pniReport = getPniReportUseCase.getPniReport(pniReportId);
        return ResponseEntity.ok(PniReportWebModel.fromDomainModel(pniReport));
    }
}
