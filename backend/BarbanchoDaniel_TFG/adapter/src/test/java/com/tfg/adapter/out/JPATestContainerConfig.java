package com.tfg.adapter.out;

import org.springframework.context.annotation.Profile;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.PostgreSQLContainer;

@Profile("test-jpa")
@Testcontainers
public class JPATestContainerConfig {
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    static {
        postgres.start();
    }

    public static PostgreSQLContainer<?> getInstance() {
        return postgres;
    }
}
