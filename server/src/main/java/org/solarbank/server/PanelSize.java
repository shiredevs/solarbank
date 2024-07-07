package org.solarbank.server;

import lombok.Data;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.NotNull;

@Data
public class PanelSize {
    @NotNull
    @Positive
    private double height;

    @NotNull
    @Positive
    private double width;
}
