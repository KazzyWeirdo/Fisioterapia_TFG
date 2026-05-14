package com.tfg.auditlog;

public record AuditLogId(int value) {
    public AuditLogId {
        if (value < 1) {
            throw new IllegalArgumentException("'value' must be a positive integer");
        }
    }
}
