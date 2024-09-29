package org.solarbank.server.unit.service;

import java.util.Map;
import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.solarbank.server.dto.CalculateRequest;
import org.solarbank.server.dto.CalculateResult;
import org.solarbank.server.dto.EnergyTariff;
import org.solarbank.server.dto.PanelSize;
import org.solarbank.server.dto.Location;
import org.solarbank.server.dto.CalculateResult.SavingsPerYear;
import org.solarbank.server.service.CalculateService;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.solarbank.server.integration.IntegrationTestBase.createCalculateRequest;
import static org.solarbank.server.integration.IntegrationTestBase.createNasaData;

import org.solarbank.server.service.NasaClient;
import org.springframework.web.reactive.function.client.WebClient;

public class CalculateServiceTest {

    private CalculateService calculateService = new CalculateService();
//    private NasaClient nasaClient;
//
//    @BeforeEach
//    void setUp() {
//        WebClient.Builder webClientBuilder = WebClient.builder().baseUrl("https://power.larc.nasa.gov/api/temporal/daily");
//        nasaClient = new NasaClient(webClientBuilder);
//    }

    @Test
    public void calculateRequest_validInputs_returnsExpectedResult() {
        CalculateRequest calculateRequest = createCalculateRequest();
        PanelSize panelSize = calculateRequest.getPanelSize();
        Double panelEfficiency = calculateRequest.getPanelEfficiency();
        EnergyTariff energyTariff = calculateRequest.getEnergyTariff();
        Location location = calculateRequest.getLocation();

        Map<String, Double> nasaData = createNasaData();

        CalculateResult result = calculateService.processCalculateRequest(panelSize, panelEfficiency, energyTariff, nasaData);

        System.out.println(result);
    }
}