package com.tfg.port.in.auditlog;

import com.tfg.auditlog.AuditLog;
import com.tfg.pojos.auditlog.PageQuery;
import com.tfg.pojos.auditlog.PagedResponse;

public interface GetAllAuditLogsUseCase {
    PagedResponse<AuditLog> getAllAuditLogs(PageQuery query);
}
