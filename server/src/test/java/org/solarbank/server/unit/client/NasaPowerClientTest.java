package org.solarbank.server.unit.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.solarbank.server.client.ApiClient;
import org.solarbank.server.client.NasaPowerClient;
import org.solarbank.server.configuration.ApplicationProperties;
import org.solarbank.server.dto.Location;
import org.solarbank.server.dto.RadianceResponse;
import org.solarbank.server.integration.WireMockConfiguration;
import org.solarbank.server.utils.JsonReader;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

public class NasaPowerClientTest {
    private static WireMockServer mockServer;
    private static NasaPowerClient client;

    @BeforeAll
    public static void setUp() {
        mockServer = new WireMockConfiguration()
            .wireMockServer();
        mockServer.start();

        ApplicationProperties.ClientProperties clientProperties = new ApplicationProperties.ClientProperties();
        clientProperties.setRetryInitialBackoff(100);
        clientProperties.setRetryMaxBackoff(1000);
        clientProperties.setRetryMaxAttempts(2);

        ApplicationProperties.Endpoint endpoint = new ApplicationProperties.Endpoint();
        endpoint.setNasaPowerBaseUrl(String.format("http://localhost:%s", mockServer.port()));

        ApplicationProperties applicationProperties = new ApplicationProperties();
        applicationProperties.setClient(clientProperties);
        applicationProperties.setEndpoint(endpoint);

        client = new NasaPowerClient(new ApiClient(applicationProperties), applicationProperties);
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
    public void getTwentyYearsOfData_validRequest_receivedExpectedResult() throws IOException {
        double latitude = 51.493518;
        double longitude = -2.6494647;
        String actualEndpointWithQueryParams = String.format(
            "/temporal/monthly/point?start=2005&end=2024&latitude=%f&longitude=%.7f&" +
                "community=re&parameters=ALLSKY_SFC_SW_DWN&format=json&units=metric&header=true&" +
                "time-standard=utc",
            latitude,
            longitude
        );
        Location location = new Location();
        location.setLatitude(latitude);
        location.setLongitude(longitude);

        mockServer.stubFor(get(urlEqualTo(actualEndpointWithQueryParams))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withStatus(200)
                .withBodyFile("twenty-year-radiance-response.json")));

        RadianceResponse actualResponse = client.getMeanDailyRadianceFor(location);
        RadianceResponse expectedResponse = JsonReader.fromClassResource(
            "/__files/twenty-year-radiance-response.json",
            RadianceResponse.class
        );

        assertEquals(expectedResponse, actualResponse);
    }
}