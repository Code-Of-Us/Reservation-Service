package com.codeofus.reservationservice.dtos;

import com.codeofus.reservationservice.dtos.PersonDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SpotDto {

    @JsonProperty
    Integer id;

    @JsonProperty
    String address;

    @JsonProperty
    LocalDateTime availability;

    @JsonProperty
    Integer capacity;

    @JsonProperty
    PersonDto renter;

    @JsonProperty
    PersonDto parker;
}
