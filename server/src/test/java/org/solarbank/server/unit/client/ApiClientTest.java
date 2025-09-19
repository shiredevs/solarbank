package org.solarbank.server.unit.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.solarbank.server.client.ApiClient;
import org.solarbank.server.configuration.ApplicationProperties;
import org.solarbank.server.integration.WireMockConfiguration;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED;
import static org.junit.jupiter.api.Assertions.*;

public class ApiClientTest {
    private static WireMockServer mockServer;
    private static ApiClient client;
    private static int port;
    private static final String testUrl = "/anEndpoint";
    private static final String jsonResponse = """
            {
            "response": "ok"
            }
            """;

    @BeforeAll
    public static void setUp() {
        mockServer = new WireMockConfiguration().wireMockServer();
        mockServer.start();
        port = mockServer.port();
        ApplicationProperties.ClientProperties clientProperties = new ApplicationProperties.ClientProperties();
        clientProperties.setRetryInitialBackoff(100);
        clientProperties.setRetryMaxBackoff(1000);
        clientProperties.setRetryMaxAttempts(2);
        ApplicationProperties applicationProperties = new ApplicationProperties();
        applicationProperties.setClient(clientProperties);
        client = new ApiClient(applicationProperties);
    }

    @AfterAll
    public static void tearDown() {
        mockServer.stop();
    }

    @BeforeEach
    public void resetWireMock() {
        mockServer.resetAll();
    }

    @Test
    public void getRequest_validRequest_receivedExpectedResult() {
        String expectedResult = jsonResponse;

        mockServer.stubFor(get(urlEqualTo(testUrl))
            .willReturn(okJson(expectedResult).withStatus(200)));

        String actualResult = client.get(String.format("http://localhost:%s/%s", port, testUrl), String.class);

        mockServer.verify(getRequestedFor(urlEqualTo(testUrl)));
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void getRequest_validRequestSucceedsOnThirdRetry_receivedExpectedResult() {
        String expectedResult = jsonResponse;
        String scenarioName = "retry-on-error";
        String firstAttempt = "attempt-1";
        String secondAttempt = "attempt-2";

        mockServer.stubFor(get(urlEqualTo(testUrl))
            .inScenario(scenarioName)
            .whenScenarioStateIs(STARTED)
            .willSetStateTo(firstAttempt)
            .willReturn(serverError()));
        mockServer.stubFor(get(urlEqualTo(testUrl))
            .inScenario(scenarioName)
            .whenScenarioStateIs(firstAttempt)
            .willSetStateTo(secondAttempt)
            .willReturn(serverError()));
        mockServer.stubFor(get(urlEqualTo(testUrl))
            .inScenario(scenarioName)
            .whenScenarioStateIs(secondAttempt)
            .willSetStateTo("attempt-3")
            .willReturn(okJson(expectedResult).withStatus(200)));

        String actualResult = client.get(String.format("http://localhost:%s/%s", port, testUrl), String.class);

        mockServer.verify(3, getRequestedFor(urlEqualTo(testUrl)));
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void getRequest_validRequestFailsOnThirdRetry_receivedExpectedException() {
        String scenarioName = "retry-on-error";
        String firstAttempt = "attempt-1";
        String secondAttempt = "attempt-2";

        mockServer.stubFor(get(urlEqualTo(testUrl))
            .inScenario(scenarioName)
            .whenScenarioStateIs(STARTED)
            .willSetStateTo(firstAttempt)
            .willReturn(serverError()));
        mockServer.stubFor(get(urlEqualTo(testUrl))
            .inScenario(scenarioName)
            .whenScenarioStateIs(firstAttempt)
            .willSetStateTo(secondAttempt)
            .willReturn(serverError()));
        mockServer.stubFor(get(urlEqualTo(testUrl))
            .inScenario(scenarioName)
            .whenScenarioStateIs(secondAttempt)
            .willSetStateTo("attempt-3")
            .willReturn(serverError()));

        WebClientResponseException exception = assertThrows(
            WebClientResponseException.class,
            () -> client.get(String.format("http://localhost:%s/%s", port, testUrl), String.class)
        );

        mockServer.verify(3, getRequestedFor(urlEqualTo(testUrl)));
        assertTrue(exception.getStatusCode().is5xxServerError());
    }
}