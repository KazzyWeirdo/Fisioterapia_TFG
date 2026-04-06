package com.tfg.adapter.out.persistence.auditlog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogMongoDataRepository extends MongoRepository<AuditLogMongoEntity, Integer> {
}
