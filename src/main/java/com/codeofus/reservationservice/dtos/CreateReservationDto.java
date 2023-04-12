package com.codeofus.reservationservice.dtos;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateReservationDto {

    Integer personId;

    Integer spotId;

    LocalDateTime createdAt;

    LocalDateTime reservedFrom;

    LocalDateTime reservedUntil;
}
