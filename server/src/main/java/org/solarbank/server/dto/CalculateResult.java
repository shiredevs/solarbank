package org.solarbank.server.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.Map;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class CalculateResult {
    private double energyGenPerYear;
    private Map<String, Double> energyGenPerMonth;
    private SavingsPerYear savingsPerYear;

    @Data
    @JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
    public static class SavingsPerYear {
        private String currencyCode;
        private double amount;
    }
}
