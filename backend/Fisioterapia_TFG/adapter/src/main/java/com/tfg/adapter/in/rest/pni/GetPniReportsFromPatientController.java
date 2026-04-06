package com.tfg.adapter.in.rest.pni;

import com.tfg.adapter.in.rest.common.PatientIdParser;
import com.tfg.patient.PatientId;
import com.tfg.pojos.pagedpojos.PageQuery;
import com.tfg.pojos.pagedpojos.PagedResponse;
import com.tfg.pojos.query.PniReportSummaryElement;
import com.tfg.port.in.pni.GetPniReportsFromPatientUseCase;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/pni")
public class GetPniReportsFromPatientController {

    private final GetPniReportsFromPatientUseCase getPniReportsFromPatientUseCase;

    public GetPniReportsFromPatientController(GetPniReportsFromPatientUseCase getPniReportsFromPatientUseCase) {
        this.getPniReportsFromPatientUseCase = getPniReportsFromPatientUseCase;
    }

    @GetMapping("/{patientId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pni reports found successfully"),
            @ApiResponse(responseCode = "204", description = "No Pni reports found for the patient"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    public ResponseEntity<List<PniReportListWebModel>> getPniReportsFromPatient(@PathVariable("patientId") String grabbedPatientId, Pageable springPageable) {
        PatientId patientId = PatientIdParser.parsePatientId(grabbedPatientId);
        PageQuery query = new PageQuery(springPageable.getPageNumber(), springPageable.getPageSize());
        PagedResponse<PniReportSummaryElement> domainPagedResponse = getPniReportsFromPatientUseCase.getPniReportsFromPatient(query, patientId);

        if (domainPagedResponse.content().isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<PniReportListWebModel> dtoContent = domainPagedResponse.content().stream()
                .map(pniReportSummaryElement -> new PniReportListWebModel(
                        pniReportSummaryElement.id(),
                        pniReportSummaryElement.date()
                ))
                .toList();

        return ResponseEntity.ok(dtoContent);
    }
}
