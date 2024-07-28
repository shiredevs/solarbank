package org.solarbank.server.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import java.util.Map;
import lombok.Data;
import org.javamoney.moneta.Money;
import org.solarbank.server.CurrencyUnitSerializer;
import org.solarbank.server.MonetaryAmountSerializer;


@Data
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class CalculateResult {
    private Double energyGenPerYear;
    private Map<String, Double> energyGenPerMonth;
    private SavingsPerYear savingsPerYear;

    @Data
    @JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
    public static class SavingsPerYear {
        @JsonSerialize(using = CurrencyUnitSerializer.class)
        private CurrencyUnit currencyCode;
        @JsonSerialize(using = MonetaryAmountSerializer.class)
        private MonetaryAmount amount;
    }
}
