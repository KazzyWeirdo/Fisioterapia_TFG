package com.tfg.adapter.out.persistence.auditlog;

import com.tfg.adapter.out.persistence.JpaTestContainerConfig;
import com.tfg.adapter.out.persistence.MongoTestContainersConfig;
import com.tfg.port.out.persistence.AuditLogRepository;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest
@ActiveProfiles("test")
public class AuditLogRepositoryTest extends AbstractAuditLogRepositoryTest {
    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> JpaTestContainerConfig.getInstance().getJdbcUrl());
        registry.add("spring.datasource.username", () -> JpaTestContainerConfig.getInstance().getUsername());
        registry.add("spring.datasource.password", () -> JpaTestContainerConfig.getInstance().getPassword());

        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.data.mongodb.uri", () -> MongoTestContainersConfig.getInstance().getReplicaSetUrl());
    }
}
