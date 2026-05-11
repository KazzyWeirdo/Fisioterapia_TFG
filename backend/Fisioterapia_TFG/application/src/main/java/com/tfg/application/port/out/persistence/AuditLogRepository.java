package com.tfg.application.port.out.persistence;


import com.tfg.model.auditlog.AuditLog;
import com.tfg.application.pojos.pagedpojos.PageQuery;
import com.tfg.application.pojos.pagedpojos.PagedResponse;

public interface AuditLogRepository {
    void save(AuditLog auditLog);

    void deleteAll();

    PagedResponse<AuditLog> findAll(PageQuery query);
}
