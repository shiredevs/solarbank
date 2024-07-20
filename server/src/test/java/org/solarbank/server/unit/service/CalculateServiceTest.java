package org.solarbank.server.unit.service;

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

import java.util.Map;

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
        assertEquals("USD", savingsPerYear.getCurrencyCode());

        MonetaryAmount expectedAmount = Money.of(1000.0, "USD");
        assertEquals(expectedAmount.getNumber().doubleValue(), savingsPerYear.getAmount(), 0.001);
    }
}
