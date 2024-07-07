package org.solarbank.server;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Data
public class Location {
    @NotNull
    @Max(180)
    @Min(-180)
    private double longitude;

    @NotNull
    @Max(90)
    @Min(-90)
    private double lat;
}
