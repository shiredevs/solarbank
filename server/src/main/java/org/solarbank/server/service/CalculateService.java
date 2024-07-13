package org.solarbank.server.service;

import java.util.HashMap;
import java.util.Map;
import org.solarbank.server.dto.CalculateRequest;
import org.springframework.stereotype.Service;

@Service
public class CalculateService {
    public Map<String, Object> processCalculateRequest(CalculateRequest calculateRequest) {

        Map<String, Double> energyGenPerMonth = new HashMap<>();
        energyGenPerMonth.put("January", 0.1);
        energyGenPerMonth.put("February", 0.2);

        Map<String, Object> savingsPerYear = new HashMap<>();
        savingsPerYear.put("currencyCode", "USD");
        savingsPerYear.put("amount", 1000.0);

        double energyGenPerYear = 1.0;

        Map<String, Object> response = new HashMap<>();
        response.put("energyGenPerYear", energyGenPerYear);
        response.put("energyGenPerMonth", energyGenPerMonth);
        response.put("savingsPerYear", savingsPerYear);

        return response;
    }
}
