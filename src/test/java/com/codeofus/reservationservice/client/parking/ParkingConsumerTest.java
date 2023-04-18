package com.codeofus.reservationservice.client.parking;

import com.codeofus.reservationservice.client.ParkingConsumer;
import com.codeofus.reservationservice.dtos.SpotDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.codeofus.reservationservice.client.ParkingMocks.setupParkingApiSpotsResponse;
import static com.codeofus.reservationservice.client.ParkingMocks.setupParkingApiToFailWithStatus;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@ActiveProfiles("test")
public class ParkingConsumerTest extends BaseParkingConsumerIntegrationTest {
    static final String RESERVATIONS_SPOTS_URL = "/api/v1/reservations/spots";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ParkingConsumer parkingConsumer;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void testCircuitBreakerRemainsInClosedState() throws Exception {
        setupParkingApiSpotsResponse();
        for (int i = 1; i <= 10; i++) {
            mockMvc.perform(get(RESERVATIONS_SPOTS_URL + "?page=0&size=10"));
        }
        assertEquals(CircuitBreaker.State.CLOSED, getCircuitBreakerStatus(PARKING_CIRCUIT_BREAKER_NAME));
    }

    @Test
    public void testCircuitBreakerTransitionToOpenStateFromClosedState() throws Exception {
        setupParkingApiToFailWithStatus(503);
        for (int i = 1; i <= 10; i++) {
            mockMvc.perform(get(RESERVATIONS_SPOTS_URL));
        }

        assertEquals(CircuitBreaker.State.OPEN, getCircuitBreakerStatus(PARKING_CIRCUIT_BREAKER_NAME));
    }

    @Test
    public void testCircuitBreakerTransitionToHalfOpenStateFromOpenState() {
        transitionToState(PARKING_CIRCUIT_BREAKER_NAME, CircuitBreaker.State.OPEN);
        await().atMost(2, TimeUnit.SECONDS).until(() -> CircuitBreaker.State.HALF_OPEN.equals(getCircuitBreakerStatus(PARKING_CIRCUIT_BREAKER_NAME)));
    }

    @Test
    public void testRetryApiCall() throws Exception {
        setupParkingApiToFailWithStatus(501);
        mockMvc.perform(MockMvcRequestBuilders.get(RESERVATIONS_SPOTS_URL));

        assertEquals(1, retryEvents.size());
    }

    @Test
    public void testRetryApiCallNotPermitted() throws Exception {
        transitionToState(PARKING_CIRCUIT_BREAKER_NAME, CircuitBreaker.State.OPEN);
        mockMvc.perform(MockMvcRequestBuilders.get(RESERVATIONS_SPOTS_URL));

        assertEquals(0, retryEvents.size());
    }

    @Test
    void getSpots() throws Exception {
        setupParkingApiSpotsResponse();

        var result = mockMvc.perform(get(RESERVATIONS_SPOTS_URL + "?page=0&size=10").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
        String jsonResponse = result.getResponse().getContentAsString();
        Page<SpotDto> spotPage = objectMapper.readValue(jsonResponse, new TypeReference<>() {});
        List<SpotDto> content = spotPage.getContent();

        assertEquals(1, content.size());
    }

    @Test
    public void testParkingConsumer() throws Exception {
        setupParkingApiSpotsResponse();
        Page<SpotDto> spot = parkingConsumer.getAllSpots(PageRequest.of(0, 10));
        assertEquals(1, spot.getTotalElements());
    }
}