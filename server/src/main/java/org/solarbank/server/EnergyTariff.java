package org.solarbank.server;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.DecimalMin;

@Data
public class EnergyTariff {
    @NotBlank
    @Size(max = 1)
    private String currencyCode;

    @NotNull
    @DecimalMin("0.01")
    private double amount;
}
