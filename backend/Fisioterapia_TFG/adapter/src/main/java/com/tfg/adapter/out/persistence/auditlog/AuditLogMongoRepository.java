package com.tfg.adapter.out.persistence.auditlog;

import com.tfg.auditlog.AuditLog;
import com.tfg.port.out.persistence.AuditLogRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class AuditLogMongoRepository implements AuditLogRepository {
    private final AuditLogMongoDataRepository auditLogMongoDataRepository;

    public AuditLogMongoRepository(AuditLogMongoDataRepository auditLogMongoDataRepository) {
        this.auditLogMongoDataRepository = auditLogMongoDataRepository;
    }

    @Override
    @Transactional
    public void save(AuditLog auditLog) {
        auditLogMongoDataRepository.save(AuditLogMongoMapper.toMongoEntity(auditLog));
    }

    @Override
    public void deleteAll() {
        auditLogMongoDataRepository.deleteAll();
    }
}
