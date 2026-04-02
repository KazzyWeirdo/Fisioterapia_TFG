package com.tfg.model.auditlog;

import com.tfg.auditlog.AuditLog;
import com.tfg.auditlog.AuditLogId;

import java.util.concurrent.ThreadLocalRandom;

public class AuditLogFactory {
    public static final AuditLogId AUDIT_LOG_ID = new AuditLogId(ThreadLocalRandom.current().nextInt(1_000_000));
    public static String ENTITY_NAME = "Hola";
    public static String ACTION = "Hola";
    public static String TIMESTAMP = "1-1-1";
    public static String DETAILS = "Hola";

    public static AuditLog createTestAuditLog(String user) {
        AuditLog auditLog = new AuditLog(
                AUDIT_LOG_ID,
                ENTITY_NAME,
                ACTION,
                TIMESTAMP,
                DETAILS,
                user
        );
        return auditLog;
    }
}
