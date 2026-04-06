package com.tfg.service.auditlog;

import com.tfg.auditlog.AuditLog;
import com.tfg.exceptions.InvalidPageOrSizeException;
import com.tfg.pojos.pagedpojos.PageQuery;
import com.tfg.pojos.pagedpojos.PagedResponse;
import com.tfg.port.in.auditlog.GetAllAuditLogsUseCase;
import com.tfg.port.out.persistence.AuditLogRepository;

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
