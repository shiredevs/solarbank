package org.solarbank.server.service;

import java.util.HashMap;
import java.util.Map;
import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import org.javamoney.moneta.Money;
import org.solarbank.server.dto.EnergyTariff;
import org.solarbank.server.dto.CalculateResult;
import org.solarbank.server.dto.CalculateResult.SavingsPerYear;
import org.solarbank.server.dto.PanelSize;
import org.springframework.stereotype.Service;

@Service
public class CalculateService {
    public CalculateResult processCalculateRequest(
            PanelSize panelSize,
            Double panelEfficiency,
            EnergyTariff energyTariff
    ) {

        Map<String, Double> energyGenPerMonth = new HashMap<>();
        energyGenPerMonth.put("January", 0.1);
        energyGenPerMonth.put("February", 0.2);

        SavingsPerYear savingsPerYear = new SavingsPerYear();
        CurrencyUnit currencyUnit = Monetary.getCurrency(energyTariff.getCurrencyCode());
        MonetaryAmount amount = Money.of(1000, currencyUnit);
        savingsPerYear.setCurrencyCode(currencyUnit);
        savingsPerYear.setAmount(amount);

        Double energyGenPerYear = 1.0;

        CalculateResult result = new CalculateResult();
        result.setEnergyGenPerMonth(energyGenPerMonth);
        result.setEnergyGenPerYear(energyGenPerYear);
        result.setSavingsPerYear(savingsPerYear);

        return result;
    }
}