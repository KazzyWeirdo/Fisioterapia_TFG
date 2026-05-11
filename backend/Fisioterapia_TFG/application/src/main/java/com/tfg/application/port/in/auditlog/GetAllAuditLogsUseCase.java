package com.tfg.application.port.in.auditlog;

import com.tfg.model.auditlog.AuditLog;
import com.tfg.application.pojos.pagedpojos.PageQuery;
import com.tfg.application.pojos.pagedpojos.PagedResponse;

public interface GetAllAuditLogsUseCase {
    PagedResponse<AuditLog> getAllAuditLogs(PageQuery query);
}
