package com.codeofus.reservationservice.kafka.consumer;

import com.codeofus.reservations.ReservationDto;
import com.codeofus.reservationservice.domain.Reservation;
import com.codeofus.reservationservice.services.ReservationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ParkingKafkaConsumer {
    Logger logger = LoggerFactory.getLogger(ParkingKafkaConsumer.class);

    ReservationService reservationService;

    @KafkaListener(topics = "${spring.kafka.reservation-topic}")
    public void consume(ConsumerRecord<String, ReservationDto> record) {
        ReservationDto reservationMessage = record.value();
        logger.info("Message received: {}", reservationMessage);

        Reservation reservationToSave = createReservationFromAvroDto(reservationMessage);
        reservationService.createReservation(reservationToSave);
        logger.info("Message consumed: {}", reservationMessage);
    }

    private LocalDateTime instantToLocalDateTime(Instant instant) {
        return instant != null ? LocalDateTime.ofInstant(instant, ZoneOffset.UTC) : null;
    }

    private Reservation createReservationFromAvroDto(ReservationDto reservationMessage) {
        return Reservation.builder()
                .personId(reservationMessage.getPersonId())
                .spotId(reservationMessage.getSpotId())
                .createdAt(instantToLocalDateTime(reservationMessage.getCreatedAt()))
                .reservedFrom(instantToLocalDateTime(reservationMessage.getReservedFrom()))
                .reservedUntil(instantToLocalDateTime(reservationMessage.getReservedUntil()))
                .build();
    }
}