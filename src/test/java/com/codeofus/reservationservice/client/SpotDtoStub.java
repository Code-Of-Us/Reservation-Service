package com.codeofus.reservationservice.client;

import com.codeofus.reservationservice.dtos.PersonDto;
import com.codeofus.reservationservice.dtos.SpotDto;

public class SpotDtoStub {
    public static SpotDto createSpotStub() {
        return createPersonStubBuilder().build();
    }

    public static SpotDto.SpotDtoBuilder createPersonStubBuilder() {
        return SpotDto.builder().id(1).address("123 Street").parkingZone("2.II").renter(PersonDto.builder().id(1).build());
    }
}
