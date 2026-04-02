package com.tfg.adapter.out.persistence.auditlog;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "auditlogs")
@Getter
@Setter
public class AuditLogMongoEntity {
    @Id
    private int id;
    private String entityName;
    private String action;
    private String timestamp;
    private String details;
}
