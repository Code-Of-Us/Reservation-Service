package com.codeofus.reservationservice;

import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import static lombok.AccessLevel.PRIVATE;

@FieldDefaults(level = PRIVATE, makeFinal = true)
@Testcontainers
@SpringBootTest
public class IntegrationTest {

    static String EUREKA_IMAGE = "ghcr.io/code-of-us/eureka-server:latest";
    static int EUREKA_PORT = 8761;

    static GenericContainer<?> eurekaServer = new GenericContainer<>(EUREKA_IMAGE)
            .withExposedPorts(EUREKA_PORT);

    @BeforeAll
    public static void startContainers() {
        eurekaServer.start();
        System.setProperty("eureka.client.service-url.defaultZone", "http://" + eurekaServer.getIpAddress() + ":" + eurekaServer.getMappedPort(EUREKA_PORT) + "/eureka");
    }

    @AfterAll
    public static void stopContainers() {
        eurekaServer.stop();
    }
}
