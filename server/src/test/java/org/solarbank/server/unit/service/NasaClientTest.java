package org.solarbank.server.unit.service;

import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.solarbank.server.dto.CalculateRequest;
import org.solarbank.server.dto.Location;
import org.solarbank.server.dto.NasaResponse;
import org.solarbank.server.service.NasaClient;
import org.springframework.web.reactive.function.client.WebClient;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.solarbank.server.integration.IntegrationTestBase.createCalculateRequest;

public class NasaClientTest {
    private NasaClient nasaClient;

    @BeforeEach
    void setUp() {
        WebClient.Builder webClientBuilder = WebClient.builder().baseUrl("https://power.larc.nasa.gov/api/temporal/daily");
        nasaClient = new NasaClient(webClientBuilder);
    }

    @Test
    public void nasaClient_validInputs_returnsExpectedResult() {
        CalculateRequest calculateRequest = createCalculateRequest();
        Location location = calculateRequest.getLocation();

        Map<String, Double> nasaData  = nasaClient.getNasaData(location);

        System.out.println(nasaData);

        assertNotNull(nasaData);
    }
}