package com.codeofus.reservationservice.mappers;


import com.codeofus.reservationservice.domain.Reservation;
import com.codeofus.reservationservice.dtos.ReservationDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    ReservationDto reservationToReservationDTO(Reservation reservation);

    Reservation reservationDTOtoReservation(ReservationDto reservationDto);

    List<ReservationDto> reservationToReservationDTO(List<Reservation> reservations);

    List<Reservation> reservationDTOtoReservation(List<ReservationDto> reservationDtoList);

}
