package org.solarbank.server.unit.service;

import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.solarbank.server.client.NasaPowerClient;
import org.solarbank.server.dto.CalculateRequest;
import org.solarbank.server.dto.CalculateResult;
import org.solarbank.server.dto.RadianceResponse;
import org.solarbank.server.service.DailyRadianceProcessor;
import org.solarbank.server.service.EnergySavingService;
import org.solarbank.server.utils.JsonReader;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.solarbank.server.integration.IntegrationTestBase.createCalculateRequest;

public class EnergySavingServiceTest {
    private EnergySavingService energySavingService;
    private NasaPowerClient client;

    @BeforeEach
    public void setup() {
        client = mock(NasaPowerClient.class);
        energySavingService = new EnergySavingService(new DailyRadianceProcessor(), client);
    }

    @Test
    public void calculateRequest_validInputs_returnsExpectedResult() throws IOException {
        CalculateRequest calculateRequest = createCalculateRequest();
        RadianceResponse response = JsonReader.fromClassResource(
            "/__files/one-year-radiance-response.json",
            RadianceResponse.class
        );
        when(client.getMeanDailyRadianceFor(calculateRequest.getLocation()))
            .thenReturn(response);

        CalculateResult result = energySavingService.calculateSavings(calculateRequest);

        // create the calculate request in the test so I know the parameters
        // do all the calculations for this response object in the test to get the correct expected values
        // call the actual code
        // compare values
    }
}