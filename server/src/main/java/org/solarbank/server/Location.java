package org.solarbank.server;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Data
public class Location {
    @NotNull
    @Max(value = 180, message = "maximum longitude = 180")
    @Min(value = -180, message = "minimum longitude = -180")
    private double longitude;

    @NotNull
    @Max(value = 90, message = "maximum latitude = 90")
    @Min(value = -90, message = "minimum longitude = -90")
    private double lat;
}
