package org.solarbank.server.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.UnknownCurrencyException;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.solarbank.server.ValidCurrencyCode;

@Data
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class EnergyTariff {

    @ValidCurrencyCode
    private String currencyCode;

    @NotNull(message = "energy tariff must be provided")
    @DecimalMin(value = "0.01", message = "amount must be at least 0.01")
    private Double amount;
}
