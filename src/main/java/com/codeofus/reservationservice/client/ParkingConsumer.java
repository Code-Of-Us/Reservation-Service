package com.codeofus.reservationservice.client;

import com.codeofus.reservationservice.dtos.SpotDto;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "PARKING-SERVICE")
public interface ParkingConsumer {
    Logger logger = LoggerFactory.getLogger(ParkingConsumer.class);

    @GetMapping("/api/v1/parking")
    @CircuitBreaker(name = "parking", fallbackMethod = "parkingApiNotPermitted")
    @Retry(name = "parking", fallbackMethod = "parkingApiNotAvailable")
    List<SpotDto> getSpots();

    default List<SpotDto> parkingApiNotAvailable(Exception e) {
        logger.debug("Parking service is not available");
        return List.of();
    }

    default List<SpotDto> parkingApiNotPermitted(CallNotPermittedException e) {
        logger.debug("Parking Api calling not permitted");
        return List.of();
    }
}
