package com.codeofus.reservationservice;

import lombok.experimental.FieldDefaults;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static lombok.AccessLevel.PRIVATE;

@FieldDefaults(level = PRIVATE, makeFinal = true)
@Testcontainers
@SpringBootTest
public class IntegrationTest {

    static String EUREKA_IMAGE = "ghcr.io/code-of-us/eureka-server";
    static int EUREKA_PORT = 8080;
    static String POSTGRES_IMAGE = "postgres:14.7-alpine";

    static PostgreSQLContainer postgres = new PostgreSQLContainer(POSTGRES_IMAGE);

    static GenericContainer<?> eurekaServer = new GenericContainer<>(DockerImageName.parse(EUREKA_IMAGE))
            .withExposedPorts(EUREKA_PORT);

    static {
        postgres.start();
        eurekaServer.start();
        System.setProperty("eureka.client.service-url.defaultZone", "http://" + eurekaServer.getIpAddress() + ":" + eurekaServer.getMappedPort(EUREKA_PORT) + "/eureka");
        System.setProperty("spring.datasource.url", postgres.getJdbcUrl());
        System.setProperty("spring.datasource.username", postgres.getUsername());
        System.setProperty("spring.datasource.password", postgres.getPassword());
    }
}
