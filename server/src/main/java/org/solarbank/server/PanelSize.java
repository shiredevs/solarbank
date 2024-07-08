package org.solarbank.server;

import lombok.Data;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.NotNull;

@Data
public class PanelSize {
    @NotNull
    @Positive(message = "Panel height must be a positive number")
    private double height;

    @NotNull
    @Positive(message = "Panel width must be a positive number")
    private double width;
}
