package com.codeofus.reservationservice;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@AutoConfigureMockMvc
public class ParkingConsumerTest extends BaseParkingConsumerIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    public void testCircuitBreakerTransitionToOpenStateFromClosedState() throws Exception {
        stubGetParkingApiToFailWithStatus(503);
        for (int i = 1; i <= 10; i++) {
            mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/reservations/spots"));
        }

        assertEquals(CircuitBreaker.State.OPEN, getCircuitBreakerStatus(PARKING_CIRCUIT_BREAKER_NAME));
    }

    @Test
    public void testCircuitBreakerTransitionToHalfOpenStateFromOpenState() {
        transitionToState(PARKING_CIRCUIT_BREAKER_NAME, CircuitBreaker.State.OPEN);
        await()
                .atMost(61, TimeUnit.SECONDS)
                .until(() -> CircuitBreaker.State.HALF_OPEN.equals(getCircuitBreakerStatus(PARKING_CIRCUIT_BREAKER_NAME)));
    }

    @Test
    public void testCircuitBreakerTransitionsToOpenStateAfterHalfOpenState() throws Exception {
        stubGetParkingApiToFailWithStatus(503);
        for (int i = 1; i <= 20; i++) {
            mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/reservations/spots"));
        }

        assertEquals(CircuitBreaker.State.OPEN, getCircuitBreakerStatus(PARKING_CIRCUIT_BREAKER_NAME));
    }

    @Test
    public void testRetryApiCall() throws Exception {
        stubGetParkingApiToFailWithStatus(503);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/reservations/spots"));

        assertEquals(4, retryEvents.size());
    }

    @Test
    public void testRetryApiCallNotPermitted() throws Exception {
        transitionToState(PARKING_CIRCUIT_BREAKER_NAME, CircuitBreaker.State.OPEN);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/reservations/spots"));

        assertEquals(0, retryEvents.size());
    }
}
