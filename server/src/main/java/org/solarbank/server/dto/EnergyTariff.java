package org.solarbank.server.dto;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import org.javamoney.moneta.Money;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class EnergyTariff {
    private CurrencyUnit currencyCode;

    @NotNull(message = "energy tariff must be provided")
    @DecimalMin(value = "0.01", message = "amount must be at least 0.01")
    private double amount;

    public CurrencyUnit getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = Monetary.getCurrency(currencyCode);
    }
}
