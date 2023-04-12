package com.codeofus.reservationservice.mappers;


import com.codeofus.reservationservice.domain.Reservation;
import com.codeofus.reservationservice.dtos.CreateReservationDto;
import com.codeofus.reservationservice.dtos.ReservationDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    ReservationDto reservationToReservationDTO(Reservation reservation);

    Reservation reservationDTOtoReservation(ReservationDto reservationDto);

    Reservation createOrUpdateDTOtoReservation(CreateReservationDto reservation);

}

