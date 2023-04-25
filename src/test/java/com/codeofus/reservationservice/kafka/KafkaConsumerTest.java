package com.codeofus.reservationservice.kafka;

import com.codeofus.reservations.ReservationDto;
import com.codeofus.reservationservice.IntegrationTest;
import com.codeofus.reservationservice.services.ReservationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class KafkaConsumerTest extends IntegrationTest {

    @Value("${spring.kafka.reservation-topic}")
    String topic;

    @MockBean
    ReservationService reservationService;

    @Autowired
    KafkaTemplate<Object, ReservationDto> kafkaTemplate;


    @Test
    public void checkIfKafkaMessagesConsumed() {
        kafkaTemplate.send(topic, ReservationDtoStub.createReservationSub());

        await()
                .atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> verify(reservationService, times(1)).createReservation(any()));
    }

}
