package com.tfg.adapter.out;

import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.MongoDBContainer;

@ActiveProfiles("test-mongo")
@Testcontainers
public class MongoTestContainerConfig {
    static final MongoDBContainer mongo = new MongoDBContainer("mongo:6.0");

    static {
        mongo.start();
    }

    public static MongoDBContainer getInstance() {
        return mongo;
    }
}
