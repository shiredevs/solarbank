package org.solarbank.server.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class PanelSize {
    @NotNull(message = "panel height must be provided")
    @Positive(message = "Panel height must be a positive number")
    private Double height;

    @NotNull(message = "panel width must be provided")
    @Positive(message = "Panel width must be a positive number")
    private Double width;
}
