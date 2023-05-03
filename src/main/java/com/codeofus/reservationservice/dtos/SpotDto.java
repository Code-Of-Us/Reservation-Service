package com.codeofus.reservationservice.dtos;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SpotDto {

    Integer id;

    String address;

    String parkingZone;

    PersonDto renter;

}
