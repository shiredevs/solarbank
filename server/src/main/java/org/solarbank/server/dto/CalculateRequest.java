package org.solarbank.server.dto;

import jakarta.validation.constraints.NotNull;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.solarbank.server.dto.CalculateResult;
import org.solarbank.server.ValidationMessage;

@Data
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class CalculateRequest {
    @Valid
    @NotNull(message = ValidationMessage.LOCATION_NULL)
    private Location location;

    @Valid
    @NotNull(message = ValidationMessage.PANEL_SIZE_NULL)
    private PanelSize panelSize;

    @NotNull(message = ValidationMessage.PANEL_EFF_NULL)
    @Positive(message = ValidationMessage.PANEL_EFF_POSITIVE)
    @DecimalMax(value = "1.00", message = ValidationMessage.PANEL_EFF_MAX)
    private Double panelEfficiency;

    @Valid
    @NotNull(message = ValidationMessage.ENERGY_TARIFF_NULL)
    private EnergyTariff energyTariff;
}