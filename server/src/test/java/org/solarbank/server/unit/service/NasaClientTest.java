package org.solarbank.server.unit.service;

import java.io.IOException;
import java.util.Map;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.solarbank.server.ErrorMessage;
import org.solarbank.server.dto.CalculateRequest;
import org.solarbank.server.dto.Location;
import org.solarbank.server.service.NasaClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.solarbank.server.integration.IntegrationTestBase.createCalculateRequest;

public class NasaClientTest {
    private NasaClient nasaClient;
    private NasaClient realNasaClient;
    private static MockWebServer mockBackEnd;

    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @BeforeEach
    void initialize() {
        String baseUrl = String.format("http://localhost:%d", mockBackEnd.getPort());
        nasaClient = new NasaClient(baseUrl);
    }

    @Test
    public void nasaClient_validInputs_returnsExpectedResult() {
        realNasaClient = new NasaClient("https://power.larc.nasa.gov/api/temporal/monthly");
        Location location = createCalculateRequest().getLocation();

        Map<String, Double> nasaData = realNasaClient.getNasaData(location);

        System.out.println(nasaData);

        assertNotNull(nasaData);
        assertEquals(130, nasaData.size());
        assertEquals(0.83, nasaData.get("201201"));
        assertEquals(1.42, nasaData.get("201502"));
        assertEquals(0.95, nasaData.get("202011"));
    }

    @Test
    public void nasaClient_notFound_throwsException() {
        for (int i = 0; i < 4; i++) {
            mockBackEnd.enqueue(new MockResponse().setResponseCode(HttpStatus.NOT_FOUND.value()));
        }

        Location location = createCalculateRequest().getLocation();

        StepVerifier.create(Mono.fromSupplier(() -> nasaClient.getNasaData(location)))
                .expectErrorMatches(throwable -> throwable.getMessage()
                        .equals("Retries exhausted: 3/3") && throwable.getCause()
                        .getMessage()
                        .contains(ErrorMessage.SOMETHING_WRONG.getMessage() + ErrorMessage.API_NOT_FOUND.getMessage()))
                .verify();
    }

    @Test
    public void nasaClient_serviceUnavailable_throwsException() {
        for (int i = 0; i < 4; i++) {
            mockBackEnd.enqueue(new MockResponse().setResponseCode(HttpStatus.SERVICE_UNAVAILABLE.value()));
        }

        Location location = createCalculateRequest().getLocation();

        StepVerifier.create(Mono.fromSupplier(() -> nasaClient.getNasaData(location)))
                .expectErrorMatches(throwable -> throwable.getMessage()
                        .equals("Retries exhausted: 3/3") && throwable.getCause()
                        .getMessage()
                        .contains(ErrorMessage.SOMETHING_WRONG.getMessage() +
                                ErrorMessage.API_NOT_RESPONDING.getMessage()))
                .verify();
    }

    @Test
    public void nasaClient_serviceUnavailableThenSuccess_retriesAndSucceeds() {
        for (int i = 0; i < 2; i++) {
            mockBackEnd.enqueue(new MockResponse().setResponseCode(HttpStatus.SERVICE_UNAVAILABLE.value()));
        }

        mockBackEnd.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.OK.value())
                .setBody("{ \"properties\": { \"parameter\": " +
                        "{ \"ALLSKY_SFC_SW_DWN\": { \"202101\": 5.2, \"202102\": 6.1 } } } }")
                .addHeader("Content-Type", "application/json"));

        Location location = createCalculateRequest().getLocation();

        StepVerifier.create(Mono.fromSupplier(() -> nasaClient.getNasaData(location)))
                .assertNext(nasaData -> {
                    assertNotNull(nasaData);
                    assertFalse(nasaData.isEmpty());
                    assertEquals(5.2, nasaData.get("202101"), 0.001);
                    assertEquals(6.1, nasaData.get("202102"), 0.001);
                })
                .verifyComplete();
    }
}