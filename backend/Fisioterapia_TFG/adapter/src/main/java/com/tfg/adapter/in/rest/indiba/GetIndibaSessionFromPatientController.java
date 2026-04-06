package com.tfg.adapter.in.rest.indiba;

import com.tfg.adapter.in.rest.common.PatientIdParser;
import com.tfg.patient.PatientId;
import com.tfg.pojos.pagedpojos.PageQuery;
import com.tfg.pojos.pagedpojos.PagedResponse;
import com.tfg.pojos.query.IndibaSummaryElement;
import com.tfg.port.in.indiba.GetIndibaSessionFromPatientUseCase;
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
@RequestMapping("/indiba")
public class GetIndibaSessionFromPatientController {

    private final GetIndibaSessionFromPatientUseCase getIndibaSessionFromPatientUseCase;

    public GetIndibaSessionFromPatientController(GetIndibaSessionFromPatientUseCase getIndibaSessionFromPatientUseCase) {
        this.getIndibaSessionFromPatientUseCase = getIndibaSessionFromPatientUseCase;
    }

    @GetMapping("/{patientId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Indiba sessions found successfully"),
            @ApiResponse(responseCode = "204", description = "No Indiba sessions found for the patient"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    public ResponseEntity<PagedResponse<IndibaListWebModel>> getIndibaSessionsFromPatient(@PathVariable("patientId") String grabbedPatientId, Pageable springPageable) {
        PatientId patientId = PatientIdParser.parsePatientId(grabbedPatientId);
        PageQuery query = new PageQuery(springPageable.getPageNumber(), springPageable.getPageSize());
        PagedResponse<IndibaSummaryElement> domainPagedResponse = getIndibaSessionFromPatientUseCase.getIndibaSessionsFromPatient(query, patientId);

        if (domainPagedResponse.content().isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<IndibaListWebModel> dtoContent = domainPagedResponse.content().stream()
                .map(indibaSummaryElement -> new IndibaListWebModel(
                        indibaSummaryElement.id(),
                        indibaSummaryElement.beginSession()
                ))
                .toList();

        PagedResponse<IndibaListWebModel> webResponse = new PagedResponse<>(
                dtoContent,
                domainPagedResponse.totalElements(),
                domainPagedResponse.totalPages(),
                domainPagedResponse.pageNumber(),
                domainPagedResponse.isLast()
        );

        return ResponseEntity.ok(webResponse);
    }
}
