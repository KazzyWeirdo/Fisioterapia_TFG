package com.tfg.adapter.out.persistence;

import org.springframework.context.annotation.Profile;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class JpaTestContainerConfig {
    static {
        System.setProperty("testcontainers.ryuk.disabled", "true");
    }
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    static {
        postgres.start();
    }

    public static PostgreSQLContainer<?> getInstance() {
        return postgres;
    }
}
