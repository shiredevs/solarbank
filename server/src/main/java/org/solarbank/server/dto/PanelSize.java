package org.solarbank.server.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class PanelSize {
    @NotNull
    @Positive(message = "Panel height must be a positive number")
    private double height;

    @NotNull
    @Positive(message = "Panel width must be a positive number")
    private double width;
}
