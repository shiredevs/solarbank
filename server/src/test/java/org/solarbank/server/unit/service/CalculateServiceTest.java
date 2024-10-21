package org.solarbank.server.unit.service;

import java.time.Month;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import org.solarbank.server.dto.CalculateRequest;
import org.solarbank.server.dto.CalculateResult;
import org.solarbank.server.dto.EnergyTariff;
import org.solarbank.server.dto.PanelSize;
import org.solarbank.server.dto.Location;
import org.solarbank.server.dto.CalculateResult.SavingsPerYear;
import org.solarbank.server.service.CalculateService;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.solarbank.server.integration.IntegrationTestBase.createCalculateRequest;
import static org.solarbank.server.integration.IntegrationTestBase.createNasaData;

public class CalculateServiceTest {

    private CalculateService calculateService = new CalculateService();

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

        assertEquals(917.4, result.getEnergyGenPerYear());

        Map<String, Double> energyGenPerMonth = result.getEnergyGenPerMonth();
        Set<String> validMonths = EnumSet.allOf(Month.class).stream()
                .map(month -> month.name().substring(0, 1).toUpperCase() + month.name().substring(1).toLowerCase())
                .collect(Collectors.toSet());
        assertTrue(energyGenPerMonth.keySet().stream().allMatch(validMonths::contains));
        assertEquals(12, energyGenPerMonth.size());
        assertEquals(23.2, energyGenPerMonth.get("January"));
        assertEquals(138.0, energyGenPerMonth.get("May"));
        assertEquals(84.2, energyGenPerMonth.get("September"));

        CalculateResult.SavingsPerYear savingsPerYear = result.getSavingsPerYear();
        CurrencyUnit expectedCurrencyUnit = Monetary.getCurrency("USD");
        MonetaryAmount expectedAmount = Money.of(458.7, expectedCurrencyUnit);
        assertEquals(expectedCurrencyUnit, savingsPerYear.getCurrencyCode());
        assertEquals(expectedAmount, savingsPerYear.getAmount());
    }
}