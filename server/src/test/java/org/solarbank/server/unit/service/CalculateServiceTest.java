package org.solarbank.server.unit.service;

import java.util.Map;
import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import org.solarbank.server.dto.CalculateRequest;
import org.solarbank.server.dto.CalculateResult;
import org.solarbank.server.dto.CalculateResult.SavingsPerYear;
import org.solarbank.server.service.CalculateService;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculateServiceTest {

    private CalculateService calculateService = new CalculateService();

    @Test
    public void calculateRequest_validInputs_returnsExpectedResult() {
        CalculateRequest calculateRequest = new CalculateRequest();
        CalculateResult result = calculateService.processCalculateRequest(calculateRequest);

        assertEquals(1.0, result.getEnergyGenPerYear());

        Map<String, Double> energyGenPerMonth = result.getEnergyGenPerMonth();
        assertEquals(0.1, energyGenPerMonth.get("January"));
        assertEquals(0.2, energyGenPerMonth.get("February"));

        CalculateResult.SavingsPerYear savingsPerYear = result.getSavingsPerYear();
        CurrencyUnit expectedCurrencyUnit = Monetary.getCurrency("USD");
        MonetaryAmount expectedAmount = Money.of(1000, expectedCurrencyUnit);
        assertEquals(expectedCurrencyUnit, savingsPerYear.getCurrencyCode());
        assertEquals(expectedAmount, savingsPerYear.getAmount());
    }
}