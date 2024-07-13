package org.solarbank.server.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import java.util.Map;
import lombok.Data;
import org.javamoney.moneta.Money;

@Data
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class CalculateResult {
    private double energyGenPerYear;
    private Map<String, Double> energyGenPerMonth;
    private SavingsPerYear savingsPerYear;

    @JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
    public static class SavingsPerYear {
        private CurrencyUnit currencyCode;
        private MonetaryAmount amount;

        public String getCurrencyCode() {
            return currencyCode.getCurrencyCode();
        }

        public void setCurrencyCode(String currencyCode) {
            this.currencyCode = Monetary.getCurrency(currencyCode);
        }

        public double getAmount() {
            return amount.getNumber().doubleValue();
        }

        public void setAmount(double amount) {
            this.amount = Money.of(amount, this.currencyCode);
        }
    }
}
