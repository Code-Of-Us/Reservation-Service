package com.codeofus.reservationservice;

import com.codeofus.reservationservice.client.ParkingConsumer;
import com.codeofus.reservationservice.domain.SpotDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

public class ReservationControllerTest extends IntegrationTest {
    private final List<SpotDto> spots = List.of(new SpotDto(1, "address", LocalDateTime.now(), 1, null, null));

    @Autowired
    private ReservationController reservationController;

    @MockBean
    private ParkingConsumer parkingConsumer;

    @Test
    public void testGetSpots() {
        doReturn(spots).when(parkingConsumer).getSpots();

        List<SpotDto> spots = reservationController.getSpots();

        assertEquals(1, spots.size());
        assertEquals(1, spots.get(0).getId());
    }
}


