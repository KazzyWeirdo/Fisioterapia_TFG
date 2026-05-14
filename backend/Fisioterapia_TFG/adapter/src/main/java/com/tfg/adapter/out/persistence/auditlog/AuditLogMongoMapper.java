package com.tfg.adapter.out.persistence.auditlog;

import com.tfg.model.auditlog.AuditLog;
import com.tfg.model.auditlog.AuditLogId;

public class AuditLogMongoMapper {

    static AuditLogMongoEntity toMongoEntity(AuditLog auditLog) {
        AuditLogMongoEntity auditLogMongoEntity = new AuditLogMongoEntity();
        auditLogMongoEntity.setId(auditLog.getId().value());
        auditLogMongoEntity.setEntityName(auditLog.getEntityName());
        auditLogMongoEntity.setAction(auditLog.getAction());
        auditLogMongoEntity.setTimestamp(auditLog.getTimestamp());
        auditLogMongoEntity.setDetails(auditLog.getDetails());
        auditLogMongoEntity.setUserName(auditLog.getUser());
        return auditLogMongoEntity;
    }

    static AuditLog toDomainEntity(AuditLogMongoEntity auditLogMongoEntity) {
        AuditLog auditLog = new AuditLog(
            new AuditLogId(auditLogMongoEntity.getId()),
            auditLogMongoEntity.getEntityName(),
            auditLogMongoEntity.getAction(),
            auditLogMongoEntity.getTimestamp(),
                auditLogMongoEntity.getDetails(),
                auditLogMongoEntity.getUserName()
        );
        return auditLog;
    }
}
