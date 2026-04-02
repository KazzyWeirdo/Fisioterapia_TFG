package com.tfg.adapter.out.persistence.auditlog;

import com.tfg.adapter.out.persistence.MongoTestContainersConfig;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest
@ActiveProfiles("test")
public class AuditLogRepositoryTest extends AbstractAuditLogRepositoryTest {
    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", () -> MongoTestContainersConfig.getInstance().getReplicaSetUrl());
    }
}
