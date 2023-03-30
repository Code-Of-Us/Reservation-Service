package com.codeofus.reservationservice.dtos;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.ZonedDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReservationDto {

    Integer id;

    Integer personId;

    Integer spotId;

    ZonedDateTime createdAt;

    ZonedDateTime reservedFrom;

    ZonedDateTime reservedUntil;
}
