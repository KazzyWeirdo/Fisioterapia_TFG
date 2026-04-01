package com.tfg.auditlog;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class AuditLog {
    private final AuditLogId id;
    private String entityName;
    private String action;
    private String timestamp;
    private String details;
}
