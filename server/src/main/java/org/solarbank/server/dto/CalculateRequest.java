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
    @NotNull(message = "Location must be provided")
    private Location location;

    @Valid
    @NotNull(message = "panel size must be provided")
    private PanelSize panelSize;

    @NotNull(message = "panel efficiency must be provided")
    @Positive(message = "Panel efficiency must be a positive number")
    @DecimalMax(value = "1.00", message = "Panel efficiency can not be above %100")
    private Double panelEfficiency;

    @Valid
    @NotNull(message = "Energy tariff must be provided")
    private EnergyTariff energyTariff;
}