package com.tfg.port.out.persistence;


import com.tfg.auditlog.AuditLog;

public interface AuditLogRepository {
    void save(AuditLog auditLog);

    void deleteAll();
}
