package com.codeofus.reservationservice.client.parking;

import com.codeofus.reservationservice.IntegrationTest;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.retry.event.RetryEvent;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;

import java.util.ArrayList;
import java.util.List;

@AutoConfigureMockMvc
@AutoConfigureWireMock
public class BaseParkingConsumerIntegrationTest extends IntegrationTest {
    protected static String PARKING_CIRCUIT_BREAKER_NAME = "parking";
    protected static String RETRY_NAME = "parking";
    List<RetryEvent> retryEvents = new ArrayList<>();
    @Autowired
    CircuitBreakerRegistry registry;
    @Autowired
    RetryRegistry retryRegistry;

    @BeforeEach
    public void setup() {
        transitionToState(PARKING_CIRCUIT_BREAKER_NAME, CircuitBreaker.State.CLOSED);
        configureRetry();
        retryEvents.clear();
    }

    protected void transitionToState(String circuitBreakerName, CircuitBreaker.State state) {
        CircuitBreaker circuitBreaker = registry.circuitBreaker(circuitBreakerName);
        if (circuitBreaker.getState() != state) {
            switch (state) {
                case OPEN -> circuitBreaker.transitionToOpenState();
                case CLOSED -> circuitBreaker.transitionToClosedState();
                case HALF_OPEN -> circuitBreaker.transitionToHalfOpenState();
            }
        }
    }

    protected CircuitBreaker.State getCircuitBreakerStatus(String circuitBreakerName) {
        CircuitBreaker circuitBreaker = registry.circuitBreaker(circuitBreakerName);
        return circuitBreaker.getState();
    }

    private void configureRetry() {
        Retry retry = retryRegistry.retry(RETRY_NAME);
        retry.getEventPublisher()
                .onRetry(retryEvents::add);
    }
}
