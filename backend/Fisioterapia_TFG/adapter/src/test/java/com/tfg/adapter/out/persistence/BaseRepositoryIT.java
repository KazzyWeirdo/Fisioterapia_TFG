package com.tfg.adapter.out.persistence;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest
@ActiveProfiles("test")
public abstract class BaseRepositoryIT {

    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> JpaTestContainerConfig.getInstance().getJdbcUrl());
        registry.add("spring.datasource.username", () -> JpaTestContainerConfig.getInstance().getUsername());
        registry.add("spring.datasource.password", () -> JpaTestContainerConfig.getInstance().getPassword());
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.data.mongodb.uri", () -> MongoTestContainersConfig.getInstance().getReplicaSetUrl());
    }
}
