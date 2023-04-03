package com.codeofus.reservationservice;

import com.github.tomakehurst.wiremock.client.WireMock;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@AutoConfigureWireMock(port = 0)
public class BaseParkingConsumerIntegrationTest extends IntegrationTest {
    protected static String CIRCUIT_BREAKER_NAME = "parking";

    @Autowired
    private CircuitBreakerRegistry registry;

    @BeforeEach
    public void setup() {
        WireMock.reset();
        transitionToState(CircuitBreaker.State.CLOSED);
    }

    protected void transitionToState(CircuitBreaker.State state) {
        CircuitBreaker circuitBreaker = registry.circuitBreaker(CIRCUIT_BREAKER_NAME);
        if (circuitBreaker.getState() != state) {
            switch (state) {
                case OPEN -> circuitBreaker.transitionToOpenState();
                case CLOSED -> circuitBreaker.transitionToClosedState();
                case HALF_OPEN -> circuitBreaker.transitionToHalfOpenState();
            }
        }
    }

    protected CircuitBreaker.State getCircuitBreakerStatus() {
        CircuitBreaker circuitBreaker = registry.circuitBreaker(CIRCUIT_BREAKER_NAME);
        return circuitBreaker.getState();
    }

    protected void stubGetParkingApiToFailWithStatus(int responseCode) {
        stubFor(get(urlPathEqualTo("/api/v1/parking"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(responseCode)));
    }

}
