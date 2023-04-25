package com.codeofus.reservationservice;

import lombok.experimental.FieldDefaults;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static lombok.AccessLevel.PRIVATE;

@Testcontainers
@SpringBootTest
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class IntegrationTest {

    static String EUREKA_IMAGE = "springcloud/eureka";
    static int EUREKA_PORT = 8761;
    static String POSTGRES_IMAGE = "postgres:14.7-alpine";
    static String KAFKA_IMAGE = "confluentinc/cp-kafka:7.3.0";
    static int KAFKA_PORT = 9092;

    static PostgreSQLContainer postgres = new PostgreSQLContainer(POSTGRES_IMAGE);

    static GenericContainer<?> eurekaServer = new GenericContainer<>(DockerImageName.parse(EUREKA_IMAGE))
            .withExposedPorts(EUREKA_PORT);

    static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse(KAFKA_IMAGE));

    static {
        postgres.start();
        eurekaServer.start();
        kafka.start();
        System.setProperty("eureka.client.service-url.defaultZone", "http://" + eurekaServer.getHost() + ":" + eurekaServer.getFirstMappedPort() + "/eureka");
        System.setProperty("spring.datasource.url", postgres.getJdbcUrl());
        System.setProperty("spring.datasource.username", postgres.getUsername());
        System.setProperty("spring.datasource.password", postgres.getPassword());
        System.setProperty("spring.kafka.properties.bootstrap.servers", kafka.getHost() + ":" + kafka.getFirstMappedPort());
    }
}
