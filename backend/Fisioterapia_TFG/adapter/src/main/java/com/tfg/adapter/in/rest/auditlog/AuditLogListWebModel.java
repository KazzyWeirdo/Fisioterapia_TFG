package com.tfg.adapter.in.rest.auditlog;

public record AuditLogListWebModel(
        Integer id,
        String entityName,
        String action,
        String timestamp,
        String details,
        String user
) {
}
