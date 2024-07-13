package org.solarbank.server.service;

import java.util.HashMap;
import java.util.Map;
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
        savingsPerYear.setCurrencyCode("GBP");
        savingsPerYear.setAmount(1000.0);

        double energyGenPerYear = 1.0;

        CalculateResult result = new CalculateResult();
        result.setEnergyGenPerYear(energyGenPerYear);
        result.setEnergyGenPerMonth(energyGenPerMonth);
        result.setSavingsPerYear(savingsPerYear);

        return result;
    }
}