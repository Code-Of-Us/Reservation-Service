package com.codeofus.reservationservice;

import com.codeofus.reservationservice.client.ParkingConsumer;
import com.codeofus.reservationservice.domain.SpotDto;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reservation")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ReservationController {
    ParkingConsumer parkingConsumer;

    @Autowired
    public ReservationController(ParkingConsumer parkingConsumer) {
        this.parkingConsumer = parkingConsumer;
    }

    @GetMapping("/spots")
    public List<SpotDto> getReservation() {
        return parkingConsumer.getSpots();
    }
}
