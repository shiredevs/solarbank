package org.solarbank.server.service;

import java.util.HashMap;
import java.util.Map;
import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import org.javamoney.moneta.Money;
import org.solarbank.server.dto.CalculateRequest;
import org.solarbank.server.dto.CalculateResult;
import org.solarbank.server.dto.CalculateResult.SavingsPerYear;
import org.springframework.stereotype.Service;

@Service
public class CalculateService {
    public CalculateResult processCalculateRequest(CalculateRequest calculateRequest) {

        Map<String, Double> energyGenPerMonth = new HashMap<>();
        energyGenPerMonth.put("January", 0.1);
        energyGenPerMonth.put("February", 0.2);

        CalculateResult.SavingsPerYear savingsPerYear = new CalculateResult.SavingsPerYear();
        CurrencyUnit currencyUnit = Monetary.getCurrency("USD");
        MonetaryAmount amount = Money.of(1000, currencyUnit);
        savingsPerYear.setCurrencyCode(currencyUnit);
        savingsPerYear.setAmount(amount);
        System.out.println(currencyUnit);
        System.out.println(amount);
        Double energyGenPerYear = 1.0;

        CalculateResult result = new CalculateResult();
        result.setEnergyGenPerYear(energyGenPerYear);
        result.setEnergyGenPerMonth(energyGenPerMonth);
        result.setSavingsPerYear(savingsPerYear);

        return result;
    }
}