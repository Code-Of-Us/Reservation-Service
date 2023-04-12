package com.codeofus.reservationservice.client;

import com.codeofus.reservationservice.dtos.SpotDto;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "PARKING-SERVICE")
public interface ParkingConsumer {
    Logger logger = LoggerFactory.getLogger(ParkingConsumer.class);

    @GetMapping("/api/v1/spots")
    @CircuitBreaker(name = "parking", fallbackMethod = "parkingApiNotPermitted")
    @Retry(name = "parking", fallbackMethod = "parkingApiNotAvailable")
    Page<SpotDto> getAllSpots(Pageable pageable);

    default Page<SpotDto> parkingApiNotAvailable(Exception e) {
        logger.debug("Parking service is not available");
        return Page.empty();
    }

    default Page<SpotDto> parkingApiNotPermitted(CallNotPermittedException e) {
        logger.debug("Parking Api calling not permitted");
        return Page.empty();
    }
}
