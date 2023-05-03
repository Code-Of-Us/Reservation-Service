package com.codeofus.reservationservice.dtos;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PersonDto {

    Integer id;

    String firstName;

    String lastName;

    String registration;
}
