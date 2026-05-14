package com.tfg.port.in.auditlog;

import com.tfg.auditlog.AuditLog;
import com.tfg.pojos.pagedpojos.PageQuery;
import com.tfg.pojos.pagedpojos.PagedResponse;

public interface GetAllAuditLogsUseCase {
    PagedResponse<AuditLog> getAllAuditLogs(PageQuery query);
}
