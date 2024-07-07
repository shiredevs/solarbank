package org.solarbank.server;

import lombok.Data;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMax;

@Data
public class UserInputDto {
    @Valid
    private Location location;

    @Valid
    private PanelSize panelSize;

    @NotNull
    @Positive
    @DecimalMax("1.00")
    private double panelEfficiency;

    @Valid
    private EnergyTariff energyTariff;
}
