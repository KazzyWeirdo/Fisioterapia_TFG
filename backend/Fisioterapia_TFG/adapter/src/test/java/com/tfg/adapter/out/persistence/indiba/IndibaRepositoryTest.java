package com.tfg.adapter.out.persistence.indiba;

import com.tfg.adapter.out.persistence.JpaTestContainerConfig;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest
@ActiveProfiles("test")
public class IndibaRepositoryTest extends AbstractIndibaRepositoryTest {
    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> JpaTestContainerConfig.getInstance().getJdbcUrl());
        registry.add("spring.datasource.username", () -> JpaTestContainerConfig.getInstance().getUsername());
        registry.add("spring.datasource.password", () -> JpaTestContainerConfig.getInstance().getPassword());

        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }
}
