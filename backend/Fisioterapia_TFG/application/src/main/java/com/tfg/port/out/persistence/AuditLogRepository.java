package com.tfg.port.out.persistence;


import com.tfg.auditlog.AuditLog;
import com.tfg.pojos.auditlog.PageQuery;
import com.tfg.pojos.auditlog.PagedResponse;

public interface AuditLogRepository {
    void save(AuditLog auditLog);

    void deleteAll();

    PagedResponse<AuditLog> findAll(PageQuery query);
}
