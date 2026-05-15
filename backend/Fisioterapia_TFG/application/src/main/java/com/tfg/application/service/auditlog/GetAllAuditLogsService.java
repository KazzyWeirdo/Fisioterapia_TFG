package com.tfg.application.service.auditlog;

import com.tfg.model.auditlog.AuditLog;
import com.tfg.application.exceptions.InvalidPageOrSizeException;
import com.tfg.application.pojos.pagedpojos.PageQuery;
import com.tfg.application.pojos.pagedpojos.PagedResponse;
import com.tfg.application.port.in.auditlog.GetAllAuditLogsUseCase;
import com.tfg.application.port.out.persistence.AuditLogRepository;

public class GetAllAuditLogsService implements GetAllAuditLogsUseCase {
    private final AuditLogRepository auditLogRepository;

    public GetAllAuditLogsService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Override
    public PagedResponse<AuditLog> getAllAuditLogs(PageQuery query) {
        if (query.page() < 0 || query.size() <= 0) {
            throw new InvalidPageOrSizeException();
        }

        return auditLogRepository.findAll(query);
    }
}
