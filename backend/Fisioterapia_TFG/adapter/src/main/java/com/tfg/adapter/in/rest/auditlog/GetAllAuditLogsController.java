package com.tfg.adapter.in.rest.auditlog;

import com.tfg.adapter.in.rest.common.PatientIdParser;
import com.tfg.adapter.in.rest.indiba.IndibaListWebModel;
import com.tfg.auditlog.AuditLog;
import com.tfg.patient.PatientId;
import com.tfg.pojos.auditlog.PageQuery;
import com.tfg.pojos.auditlog.PagedResponse;
import com.tfg.port.in.auditlog.GetAllAuditLogsUseCase;
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
@RequestMapping("/auditlogs")
public class GetAllAuditLogsController {

    private final GetAllAuditLogsUseCase getAllAuditLogsUseCase;

    public GetAllAuditLogsController(GetAllAuditLogsUseCase getAllAuditLogsUseCase) {
        this.getAllAuditLogsUseCase = getAllAuditLogsUseCase;
    }

    @GetMapping("/list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Audit Logs displayed successfully"),
            @ApiResponse(responseCode = "204", description = "No Audit Logs found")
    })
    public ResponseEntity<PagedResponse<AuditLogListWebModel>> getLogs(Pageable springPageable) {
        PageQuery query = new PageQuery(springPageable.getPageNumber(), springPageable.getPageSize());

        PagedResponse<AuditLog> domainPagedResponse = getAllAuditLogsUseCase.getAllAuditLogs(query);

        if (domainPagedResponse.content().isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<AuditLogListWebModel> dtoContent = domainPagedResponse.content().stream()
                .map(auditLog -> new AuditLogListWebModel(
                        auditLog.getId().value(),
                        auditLog.getEntityName(),
                        auditLog.getAction(),
                        auditLog.getTimestamp(),
                        auditLog.getDetails(),
                        auditLog.getUser()
                ))
                .toList();

        PagedResponse<AuditLogListWebModel> webResponse = new PagedResponse<>(
                dtoContent,
                domainPagedResponse.totalElements(),
                domainPagedResponse.totalPages(),
                domainPagedResponse.pageNumber(),
                domainPagedResponse.isLast()
        );

        return ResponseEntity.ok(webResponse);
    }
}
