package com.codeofus.reservationservice;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

        assertEquals(CircuitBreaker.State.OPEN, getCircuitBreakerStatus());
    }

    @Test
    public void testCircuitBreakerTransitionToHalfOpenStateFromOpenState() throws InterruptedException {
        transitionToState(CircuitBreaker.State.OPEN);
        TimeUnit.SECONDS.sleep(61);

        assertEquals(CircuitBreaker.State.HALF_OPEN, getCircuitBreakerStatus());
    }

    @Test
    public void testCircuitBreakerTransitionsToOpenStateAfterHalfOpenState() throws Exception {
        stubGetParkingApiToFailWithStatus(503);
        for (int i = 1; i <= 20; i++) {
            mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/reservations/spots"));
        }

        assertEquals(CircuitBreaker.State.OPEN, getCircuitBreakerStatus());
    }

    @Test
    public void testRetryApiCall() throws Exception {
        stubGetParkingApiToFailWithStatus(503);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/reservations/spots"));

        assertEquals(4, retryEvents.size());
    }

    @Test
    public void testRetryApiCallNotPermitted() throws Exception {
        transitionToState(CircuitBreaker.State.OPEN);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/reservations/spots"));

        assertEquals(0, retryEvents.size());
    }
}
