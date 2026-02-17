package com.tfg.adapter.out.persistence;

import org.springframework.context.annotation.Profile;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class JpaTestContainerConfig {
    private static PostgreSQLContainer<?> container;

    public static synchronized PostgreSQLContainer<?> getInstance() {
        if (container == null) {
            container = new PostgreSQLContainer<>("postgres:15-alpine")
                    .withReuse(true);

            container.start();
        }
        return container;
    }
}
