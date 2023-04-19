package com.codeofus.reservationservice.kafka.consumer;

import com.codeofus.reservationservice.dtos.ReservationDto;
import com.codeofus.reservationservice.mappers.ReservationMapper;
import com.codeofus.reservationservice.services.ReservationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ParkingKafkaConsumer {
    private final Logger logger = LoggerFactory.getLogger(ParkingKafkaConsumer.class);
    private static final String RESERVATION_TOPIC = "reservations";

    private final ReservationService reservationService;

    private final ReservationMapper reservationMapper;

    private final ObjectMapper objectMapper;

    public ParkingKafkaConsumer(ReservationService reservationService, ReservationMapper reservationMapper, ObjectMapper objectMapper) {
        this.reservationService = reservationService;
        this.reservationMapper = reservationMapper;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = RESERVATION_TOPIC)
    public void consume(String message) throws JsonProcessingException {
        logger.info(String.format("Message received -> %s", message));
        ReservationDto reservationDto = objectMapper.readValue(message, ReservationDto.class);
        reservationService.createReservation(reservationMapper.reservationDTOtoReservation(reservationDto));
        logger.info("Message consumed..");
    }
}
