package org.solarbank.server;

import org.solarbank.server.UserInputDto;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CalculateService {

    public Map<String, Object> processUserInput(UserInputDto userInputDto) {
        // Mock data for energy generation per year
        double energyGenPerYear = 1.0;

        // Mock data for energy generation per month
        Map<String, Double> energyGenPerMonth = new HashMap<>();
        energyGenPerMonth.put("January", 0.1);
        energyGenPerMonth.put("February", 0.2);

        // Mock data for savings per year
        Map<String, Object> savingsPerYear = new HashMap<>();
        savingsPerYear.put("currencyCode", "USD");
        savingsPerYear.put("amount", 1000.0);

        // Combine all mock data into a single map
        Map<String, Object> response = new HashMap<>();
        response.put("energyGenPerYear", energyGenPerYear);
        response.put("energyGenPerMonth", energyGenPerMonth);
        response.put("savingsPerYear", savingsPerYear);

        return response;
    }
}
