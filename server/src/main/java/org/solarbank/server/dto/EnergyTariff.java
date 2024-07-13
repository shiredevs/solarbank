package org.solarbank.server.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class EnergyTariff {
    @NotBlank(message = "currency code must not be empty")
    private String currencyCode;

    @NotNull(message = "energy tariff must be provided")
    @DecimalMin(value = "0.01", message = "amount must be at least 0.01")
    private double amount;
}
