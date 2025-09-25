package org.solarbank.server.unit.service;

import java.io.IOException;
import java.time.Month;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.solarbank.server.client.NasaPowerClient;
import org.solarbank.server.dto.CalculateRequest;
import org.solarbank.server.dto.CalculateResult;
import org.solarbank.server.dto.MeanDailyRadiance;
import org.solarbank.server.dto.RadianceResponse;
import org.solarbank.server.service.DailyRadianceProcessor;
import org.solarbank.server.service.DailyRadianceProcessor.TotalMeanDailyRadiance;
import org.solarbank.server.service.EnergySavingService;
import org.solarbank.server.utils.JsonReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.solarbank.server.utils.RequestHelper.createCalculateRequest;

public class EnergySavingServiceTest {
    private EnergySavingService energySavingService;
    private NasaPowerClient client;
    private DailyRadianceProcessor dailyRadianceProcessor;

    private static final double TWO_DP = 0.01;

    @BeforeEach
    public void setup() {
        client = mock(NasaPowerClient.class);
        dailyRadianceProcessor = new DailyRadianceProcessor();
        energySavingService = new EnergySavingService(dailyRadianceProcessor, client);
    }

    @Test
    public void calculateRequest_twentyYearsOfDataAvailable_returnsExpectedResult() throws IOException {
        double latitude = 51.493518;
        double longitude = -2.6494647;
        double panelEfficiency = 0.9;
        double panelHeight = 3.0;
        double panelWidth = 2.0;
        double energyTariff = 0.28;
        String currencyCode = "GBP";
        CalculateRequest calculateRequest = createCalculateRequest(
            latitude,
            longitude,
            panelEfficiency,
            panelHeight, panelWidth,
            energyTariff,
            currencyCode
        );
        RadianceResponse expectedResponse = JsonReader.fromClassResource(
            "/__files/twenty-year-radiance-response.json",
            RadianceResponse.class
        );
        when(client.getMeanDailyRadianceFor(calculateRequest.getLocation()))
            .thenReturn(expectedResponse);

        CalculateResult actualResult = energySavingService.calculateSavings(calculateRequest);

        Map<Month, TotalMeanDailyRadiance> expectedTotalRadiance = calculateExpectedTotalRadiance(expectedResponse);
        double expectedGenPerYear = assertMonthlyGeneration(
            actualResult,
            expectedTotalRadiance,
            panelHeight,
            panelWidth,
            panelEfficiency
        );
        double actualGenPerYear = actualResult.getEnergyGenPerYear();

        assertEquals(expectedGenPerYear, actualGenPerYear, TWO_DP);
        assertEquals(
            expectedGenPerYear * energyTariff,
            actualResult.getSavingsPerYear().getAmount().getNumber().doubleValue(),
            TWO_DP
        );
    }

    @Test
    public void calculateRequest_noDataAvailable_returnsExpectedResult() throws IOException {
        double latitude = 51.493518;
        double longitude = -2.6494647;
        double panelEfficiency = 0.9;
        double panelHeight = 3.0;
        double panelWidth = 2.0;
        double energyTariff = 0.28;
        String currencyCode = "GBP";
        CalculateRequest calculateRequest = createCalculateRequest(
            latitude,
            longitude,
            panelEfficiency,
            panelHeight, panelWidth,
            energyTariff,
            currencyCode
        );
        RadianceResponse expectedResponse = JsonReader.fromClassResource(
            "/__files/one-year-radiance-response-no-data.json",
            RadianceResponse.class
        );
        when(client.getMeanDailyRadianceFor(calculateRequest.getLocation()))
            .thenReturn(expectedResponse);

        CalculateResult actualResult = energySavingService.calculateSavings(calculateRequest);

        assertEquals(0.0, actualResult.getEnergyGenPerYear());
        assertEquals(0.0, actualResult.getSavingsPerYear().getAmount().getNumber().doubleValue());
    }

    @Test
    public void calculateRequest_radianceParameterMissing_returnsExpectedResult() throws IOException {
        double latitude = 51.493518;
        double longitude = -2.6494647;
        double panelEfficiency = 0.9;
        double panelHeight = 3.0;
        double panelWidth = 2.0;
        double energyTariff = 0.28;
        String currencyCode = "GBP";
        CalculateRequest calculateRequest = createCalculateRequest(
            latitude,
            longitude,
            panelEfficiency,
            panelHeight, panelWidth,
            energyTariff,
            currencyCode
        );
        RadianceResponse expectedResponse = JsonReader.fromClassResource(
            "/__files/one-year-radiance-response-parameter-missing.json",
            RadianceResponse.class
        );
        when(client.getMeanDailyRadianceFor(calculateRequest.getLocation()))
            .thenReturn(expectedResponse);

        CalculateResult actualResult = energySavingService.calculateSavings(calculateRequest);

        assertEquals(0.0, actualResult.getEnergyGenPerYear());
        assertEquals(0.0, actualResult.getSavingsPerYear().getAmount().getNumber().doubleValue());
    }

    @Test
    public void calculateRequest_propertiesParameterMissing_returnsExpectedResult() throws IOException {
        double latitude = 51.493518;
        double longitude = -2.6494647;
        double panelEfficiency = 0.9;
        double panelHeight = 3.0;
        double panelWidth = 2.0;
        double energyTariff = 0.28;
        String currencyCode = "GBP";
        CalculateRequest calculateRequest = createCalculateRequest(
            latitude,
            longitude,
            panelEfficiency,
            panelHeight, panelWidth,
            energyTariff,
            currencyCode
        );
        RadianceResponse expectedResponse = JsonReader.fromClassResource(
            "/__files/one-year-radiance-response-properties-missing.json",
            RadianceResponse.class
        );
        when(client.getMeanDailyRadianceFor(calculateRequest.getLocation()))
            .thenReturn(expectedResponse);

        CalculateResult actualResult = energySavingService.calculateSavings(calculateRequest);

        assertEquals(0.0, actualResult.getEnergyGenPerYear());
        assertEquals(0.0, actualResult.getSavingsPerYear().getAmount().getNumber().doubleValue());
    }

    @Test
    public void calculateRequest_panelEfficiencyZero_returnsExpectedResult() throws IOException {
        double latitude = 51.493518;
        double longitude = -2.6494647;
        double panelEfficiency = 0.0;
        double panelHeight = 3.0;
        double panelWidth = 2.0;
        double energyTariff = 0.28;
        String currencyCode = "GBP";
        CalculateRequest calculateRequest = createCalculateRequest(
            latitude,
            longitude,
            panelEfficiency,
            panelHeight, panelWidth,
            energyTariff,
            currencyCode
        );
        RadianceResponse expectedResponse = JsonReader.fromClassResource(
            "/__files/twenty-year-radiance-response.json",
            RadianceResponse.class
        );
        when(client.getMeanDailyRadianceFor(calculateRequest.getLocation()))
            .thenReturn(expectedResponse);

        CalculateResult actualResult = energySavingService.calculateSavings(calculateRequest);

        assertEquals(0.0, actualResult.getEnergyGenPerYear());
        assertEquals(0.0, actualResult.getSavingsPerYear().getAmount().getNumber().doubleValue());
    }

    @Test
    public void calculateRequest_panelSizeZero_returnsExpectedResult() throws IOException {
        double latitude = 51.493518;
        double longitude = -2.6494647;
        double panelEfficiency = 0.9;
        double panelHeight = 0.0;
        double panelWidth = 0.0;
        double energyTariff = 0.28;
        String currencyCode = "GBP";
        CalculateRequest calculateRequest = createCalculateRequest(
            latitude,
            longitude,
            panelEfficiency,
            panelHeight, panelWidth,
            energyTariff,
            currencyCode
        );
        RadianceResponse expectedResponse = JsonReader.fromClassResource(
            "/__files/twenty-year-radiance-response.json",
            RadianceResponse.class
        );
        when(client.getMeanDailyRadianceFor(calculateRequest.getLocation()))
            .thenReturn(expectedResponse);

        CalculateResult actualResult = energySavingService.calculateSavings(calculateRequest);

        assertEquals(0.0, actualResult.getEnergyGenPerYear());
        assertEquals(0.0, actualResult.getSavingsPerYear().getAmount().getNumber().doubleValue());
    }

    @Test
    public void calculateRequest_energyTariffZero_returnsExpectedResult() throws IOException {
        double latitude = 51.493518;
        double longitude = -2.6494647;
        double panelEfficiency = 0.9;
        double panelHeight = 3.0;
        double panelWidth = 2.0;
        double energyTariff = 0.0;
        String currencyCode = "GBP";
        CalculateRequest calculateRequest = createCalculateRequest(
            latitude,
            longitude,
            panelEfficiency,
            panelHeight, panelWidth,
            energyTariff,
            currencyCode
        );
        RadianceResponse expectedResponse = JsonReader.fromClassResource(
            "/__files/twenty-year-radiance-response.json",
            RadianceResponse.class
        );
        when(client.getMeanDailyRadianceFor(calculateRequest.getLocation()))
            .thenReturn(expectedResponse);

        CalculateResult actualResult = energySavingService.calculateSavings(calculateRequest);

        assertTrue(actualResult.getEnergyGenPerYear() > 0.0);
        assertEquals(0.0, actualResult.getSavingsPerYear().getAmount().getNumber().doubleValue());
    }

    private Map<Month, TotalMeanDailyRadiance> calculateExpectedTotalRadiance(RadianceResponse expectedResponse) {
        MeanDailyRadiance expectedRadiance = expectedResponse
            .getProperties()
            .getParameter()
            .values()
            .stream()
            .findFirst()
            .orElseGet(() -> new MeanDailyRadiance(Collections.emptyMap()));

        return dailyRadianceProcessor.calculateTotalMeanDailyRadianceByMonth(expectedRadiance);
    }

    private double assertMonthlyGeneration(
        CalculateResult actualResult,
        Map<Month, TotalMeanDailyRadiance> expectedTotalRadiance,
        double panelHeight,
        double panelWidth,
        double panelEfficiency
    ) {
        Map<Month, Double> expectedMonthlyGeneration = new HashMap<>();

        actualResult.getEnergyGenPerMonth().forEach((month, actualGeneration) -> {
            Month currentMonth = Month.valueOf(month.toUpperCase());
            double expectedMeanDailyRadiance = dailyRadianceProcessor
                .calculateMeanDailyRadianceFor(currentMonth, expectedTotalRadiance.get(currentMonth));
            double expectedGeneration = expectedMeanDailyRadiance * panelHeight * panelWidth * panelEfficiency;
            expectedMonthlyGeneration.put(currentMonth, expectedGeneration);

            assertEquals(expectedGeneration, actualGeneration, TWO_DP);
        });

        return expectedMonthlyGeneration
            .values()
            .stream()
            .mapToDouble(Double::doubleValue)
            .sum();
    }
}