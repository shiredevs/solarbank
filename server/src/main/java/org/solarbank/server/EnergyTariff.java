package org.solarbank.server;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.DecimalMin;

@Data
public class EnergyTariff {
    @NotBlank
    private String currencyCode;

    @NotNull
    @DecimalMin(value = "0.01", message = "amount must be at least 0.01")
    private double amount;
}
