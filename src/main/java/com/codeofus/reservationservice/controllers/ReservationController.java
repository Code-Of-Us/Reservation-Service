package com.codeofus.reservationservice.controllers;

import com.codeofus.reservationservice.client.ParkingConsumer;
import com.codeofus.reservationservice.domain.Reservation;
import com.codeofus.reservationservice.dtos.CreateReservationDto;
import com.codeofus.reservationservice.dtos.ReservationDto;
import com.codeofus.reservationservice.dtos.SpotDto;
import com.codeofus.reservationservice.mappers.ReservationMapper;
import com.codeofus.reservationservice.services.ReservationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/reservations")
public class ReservationController {

    ParkingConsumer parkingConsumer;

    ReservationService reservationService;

    ReservationMapper reservationMapper;

    @GetMapping
    public Page<ReservationDto> getAllReservations(Pageable pageable) {
        return reservationService.getAllReservations(pageable).map(reservationMapper::reservationToReservationDTO);
    }

    @GetMapping("/{id}")
    public ReservationDto getReservation(@PathVariable int id) {
        Reservation reservation = reservationService.getReservation(id);
        return reservationMapper.reservationToReservationDTO(reservation);
    }

    @PostMapping
    public ReservationDto createReservation(@RequestBody CreateReservationDto reservationDto) {
        Reservation createdReservation = reservationService.createReservation(reservationMapper.createOrUpdateDTOtoReservation(reservationDto));
        return reservationMapper.reservationToReservationDTO(createdReservation);
    }

    @PutMapping
    public ReservationDto updateReservation(@RequestBody ReservationDto reservationDto) {
        Reservation updatedReservation = reservationService.updateReservation(reservationMapper.reservationDTOtoReservation(reservationDto));
        return reservationMapper.reservationToReservationDTO(updatedReservation);
    }

    @DeleteMapping("/{id}")
    public void deleteReservation(@PathVariable int id) {
        reservationService.deleteReservation(id);
    }

    @GetMapping("/spots")
    public Page<SpotDto> getSpots(Pageable pageable) {
        return parkingConsumer.getAllSpots(pageable);
    }
}