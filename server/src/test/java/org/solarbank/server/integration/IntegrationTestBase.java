package org.solarbank.server.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.BeforeEach;
import org.solarbank.server.dto.CalculateRequest;
import org.solarbank.server.dto.EnergyTariff;
import org.solarbank.server.dto.Location;
import org.solarbank.server.dto.PanelSize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Import(value={WireMockConfiguration.class})
public class IntegrationTestBase {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected WireMockServer mockServer;

    protected final String FAIL_MESSAGE = "failed to perform test api requests as expected";
    private final ObjectMapper requestMapper = new ObjectMapper();

    @BeforeEach
    public void resetWireMock() {
        mockServer.resetAll();
    }

    public String mapToString(CalculateRequest calculateRequest) throws JsonProcessingException {
        return requestMapper.writeValueAsString(calculateRequest);
    }

    public static CalculateRequest createCalculateRequest() {
        return createCalculateRequest(90.0, 45.0);
    }

    public static CalculateRequest createCalculateRequest(double latitude, double longitude) {
        Location location = new Location();
        location.setLatitude(latitude);
        location.setLongitude(longitude);

        PanelSize panelSize = new PanelSize();
        panelSize.setHeight(2.0);
        panelSize.setWidth(3.0);

        Double panelEfficiency = 0.15;

        EnergyTariff energyTariff = new EnergyTariff();
        energyTariff.setAmount(1.0);
        energyTariff.setCurrencyCode("USD");

        CalculateRequest calculateRequest = new CalculateRequest();
        calculateRequest.setLocation(location);
        calculateRequest.setPanelSize(panelSize);
        calculateRequest.setPanelEfficiency(panelEfficiency);
        calculateRequest.setEnergyTariff(energyTariff);

        return calculateRequest;
    }
}
