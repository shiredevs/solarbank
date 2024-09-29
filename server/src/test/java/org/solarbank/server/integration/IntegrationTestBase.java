package org.solarbank.server.integration;

import java.util.Map;
import java.util.HashMap;
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
    protected final String FAIL_MESSAGE = "failed to perform test api requests as expected";
    private final ObjectMapper requestMapper = new ObjectMapper();

    public String mapToString(CalculateRequest calculateRequest) throws JsonProcessingException {
        return requestMapper.writeValueAsString(calculateRequest);
    }

    public static CalculateRequest createCalculateRequest() {
        Location location = new Location();
        location.setLatitude(52.0);
        location.setLongitude(-2.6);

        PanelSize panelSize = new PanelSize();
        panelSize.setHeight(2.0);
        panelSize.setWidth(3.0);

        Double panelEfficiency = 0.15;

        EnergyTariff energyTariff = new EnergyTariff();
        energyTariff.setAmount(0.50);
        energyTariff.setCurrencyCode("USD");

        CalculateRequest calculateRequest = new CalculateRequest();
        calculateRequest.setLocation(location);
        calculateRequest.setPanelSize(panelSize);
        calculateRequest.setPanelEfficiency(panelEfficiency);
        calculateRequest.setEnergyTariff(energyTariff);

        return calculateRequest;
    }

    public static Map<String, Double> createNasaData() {
        Map<String, Double> nasaData = new HashMap<>();
        nasaData.put("201101", 0.83);
        nasaData.put("201102", 1.16);
        nasaData.put("201103", 2.83);
        nasaData.put("201104", 4.44);
        nasaData.put("201105", 4.81);
        nasaData.put("201106", 5.37);
        nasaData.put("201107", 4.7);
        nasaData.put("201108", 3.82);
        nasaData.put("201109", 3.04);
        nasaData.put("201110", 1.79);
        nasaData.put("201111", 0.89);
        nasaData.put("201112", 0.58);
        nasaData.put("201113", 2.86);
        nasaData.put("201201", 0.83);
        nasaData.put("201202", 1.4);
        nasaData.put("201203", 2.95);
        nasaData.put("201204", 3.49);
        nasaData.put("201205", 5.08);
        nasaData.put("201206", 4.01);
        nasaData.put("201207", 4.54);
        nasaData.put("201208", 3.71);
        nasaData.put("201209", 3.2);
        nasaData.put("201210", 1.81);
        nasaData.put("201211", 0.94);
        nasaData.put("201212", 0.61);
        nasaData.put("201213", 2.72);

        return nasaData;
    }
}
