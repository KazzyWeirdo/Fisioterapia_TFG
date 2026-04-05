package com.tfg.port.out.persistence;


import com.tfg.auditlog.AuditLog;
import com.tfg.pojos.pagedpojos.PageQuery;
import com.tfg.pojos.pagedpojos.PagedResponse;

public interface AuditLogRepository {
    void save(AuditLog auditLog);

    void deleteAll();

    PagedResponse<AuditLog> findAll(PageQuery query);
}
