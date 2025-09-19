package org.solarbank.server.unit.dto;

import org.junit.jupiter.api.Test;
import org.solarbank.server.dto.MeanDailyRadiance;
import org.solarbank.server.dto.RadianceResponse;
import org.solarbank.server.utils.JsonReader;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class RadianceResponseTest {

    @Test
    public void requestToNasaApi_validResponse_mapsToRadianceResponseCorrectly() throws IOException {
        RadianceResponse response = JsonReader.fromClassResource(
            "/__files/one-year-radiance-response.json",
            RadianceResponse.class
        );

        assertNotNull(response);
        Map<String, MeanDailyRadiance> radianceResponse = response.getProperties().getParameter();
        assertNotNull(radianceResponse);
        assertEquals(1, radianceResponse.size());
        Map<String, Double> radianceResult = radianceResponse
            .values()
            .iterator()
            .next()
            .meanDailyRadianceByMonthAndYear();
        assertNotNull(radianceResult);
        assertEquals(13, radianceResult.size());
    }

    @Test
    public void requestToNasaApi_validResponseNoData_mapsToRadianceResponseCorrectly() throws IOException {
        RadianceResponse response = JsonReader.fromClassResource(
            "/__files/one-year-radiance-response-no-data.json",
            RadianceResponse.class
        );

        assertNotNull(response);
        Map<String, MeanDailyRadiance> radianceResponse = response.getProperties().getParameter();
        assertNotNull(radianceResponse);
        assertEquals(1, radianceResponse.size());
        Map<String, Double> radianceResult = radianceResponse
            .values()
            .iterator()
            .next()
            .meanDailyRadianceByMonthAndYear();
        assertNotNull(radianceResult);
        assertEquals(0, radianceResult.size());
    }
}