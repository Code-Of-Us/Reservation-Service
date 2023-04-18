package com.codeofus.reservationservice.client;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static java.nio.charset.Charset.defaultCharset;
import static org.springframework.util.StreamUtils.copyToString;


public class ParkingMocks {
    public static void setupParkingApiSpotsResponse() throws IOException {
        stubFor(WireMock.get(WireMock.urlEqualTo("/api/v1/spots?page=0&size=10"))
                .willReturn(
                        WireMock.aResponse()
                                .withStatus(HttpStatus.OK.value())
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                .withBody(
                                        copyToString(
                                                ParkingMocks.class.getClassLoader().getResourceAsStream("payload/get-spots-response.json"),
                                                defaultCharset())
                                )
                )
        );
    }

    public static void setupParkingApiToFailWithStatus(int responseCode) {
        stubFor(WireMock.get(WireMock.urlEqualTo("/api/v1/spots"))
                .willReturn(
                        WireMock.aResponse()
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                .withStatus(responseCode)));

    }

}
