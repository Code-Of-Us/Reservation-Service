package com.codeofus.reservationservice.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
