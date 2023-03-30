package com.codeofus.reservationservice.controllers;


import com.codeofus.reservationservice.client.ParkingConsumer;
import com.codeofus.reservationservice.domain.Reservation;
import com.codeofus.reservationservice.dtos.ReservationDto;
import com.codeofus.reservationservice.dtos.SpotDto;
import com.codeofus.reservationservice.errors.BadRequestException;
import com.codeofus.reservationservice.mappers.ReservationMapper;
import com.codeofus.reservationservice.services.ReservationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/reservations")
public class ReservationController {

    ParkingConsumer parkingConsumer;

    ReservationService reservationService;

    ReservationMapper reservationMapper;

    @GetMapping
    public List<ReservationDto> getAllReservations(Pageable pageable) {
        return reservationMapper.reservationToReservationDTO(reservationService.getAllReservations(pageable));
    }

    @GetMapping("/{id}")
    public ReservationDto getReservation(@PathVariable int id) throws BadRequestException {
        Optional<Reservation> reservation = reservationService.getReservation(id);
        if (reservation.isPresent()) {
            return reservationMapper.reservationToReservationDTO(reservation.get());
        } else {
            throw new BadRequestException("Reservation does not exist", "reservations", "does-not-exist");
        }
    }

    @PostMapping
    public ReservationDto createReservation(@RequestBody ReservationDto reservationDto) throws BadRequestException {
        if (reservationDto.getId() != null) {
            throw new BadRequestException("A new reservation cannot already have an ID", "reservations", "id-exists");
        }
        Reservation createdReservation = reservationService.createReservation(reservationMapper.reservationDTOtoReservation(reservationDto));
        return reservationMapper.reservationToReservationDTO(createdReservation);
    }

    @PutMapping
    public ReservationDto updateReservation(@RequestBody ReservationDto reservationDto) throws BadRequestException {
        Optional<Reservation> updatedReservation = reservationService.updateReservation(reservationMapper.reservationDTOtoReservation(reservationDto));
        if (updatedReservation.isPresent()) {
            return reservationMapper.reservationToReservationDTO(updatedReservation.get());
        } else {
            throw new BadRequestException("An existing reservation must have an ID", "reservations", "id-does-not-exist");
        }
    }

    @DeleteMapping("/{id}")
    public void deleteReservation(@PathVariable int id) {
        reservationService.deleteReservation(id);
    }

    @GetMapping("/spots")
    public List<SpotDto> getSpots() {
        return parkingConsumer.getSpots();
    }
}
