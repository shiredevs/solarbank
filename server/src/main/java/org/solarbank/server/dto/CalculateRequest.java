package org.solarbank.server.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class CalculateRequest {
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
