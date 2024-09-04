package org.solarbank.server.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.solarbank.server.dto.CalculateRequest;
import org.solarbank.server.dto.EnergyTariff;
import org.solarbank.server.dto.Location;
import org.solarbank.server.dto.PanelSize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class IntegrationTestBase {
    @Autowired
    protected MockMvc mockMvc;

    private final ObjectMapper requestMapper = new ObjectMapper();

    public String mapToString(CalculateRequest calculateRequest) throws JsonProcessingException {
        return requestMapper.writeValueAsString(calculateRequest);
    }

    public static CalculateRequest createCalculateRequest() {
        Location location = new Location();
        location.setLatitude(90.0);
        location.setLongitude(45.0);

        PanelSize panelSize = new PanelSize();
        panelSize.setHeight(2.0);
        panelSize.setWidth(3.0);

        Double panelEfficiency = 0.15;

        EnergyTariff energyTariff = new EnergyTariff();
        energyTariff.setAmount(0.01);
        energyTariff.setCurrencyCode("USD");

        CalculateRequest calculateRequest = new CalculateRequest();
        calculateRequest.setLocation(location);
        calculateRequest.setPanelSize(panelSize);
        calculateRequest.setPanelEfficiency(panelEfficiency);
        calculateRequest.setEnergyTariff(energyTariff);

        return calculateRequest;
    }
}
