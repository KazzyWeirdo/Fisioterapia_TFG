package com.tfg.adapter.out.persistence;

import org.springframework.context.annotation.Profile;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Profile("test")
@Testcontainers
public class MongoTestContainersConfig {
    static final MongoDBContainer mongo = new MongoDBContainer("mongo:6.0");

    static {
        mongo.start();
    }

    public static MongoDBContainer getInstance() {
        return mongo;
    }
}
