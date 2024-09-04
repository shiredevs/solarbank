package org.solarbank.server.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.solarbank.server.ValidationMessage;

@Data
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class PanelSize {
    @NotNull(message = ValidationMessage.PANEL_HEIGHT_NULL)
    @Positive(message = ValidationMessage.PANEL_HEIGHT_POSITIVE)
    private Double height;

    @NotNull(message = ValidationMessage.PANEL_WIDTH_NULL)
    @Positive(message = ValidationMessage.PANEL_WIDTH_POSITIVE)
    private Double width;
}
