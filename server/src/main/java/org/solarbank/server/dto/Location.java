package org.solarbank.server.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class Location {
    @NotNull(message = "longitude must be provided")
    @Max(value = 180, message = "maximum longitude = 180")
    @Min(value = -180, message = "minimum longitude = -180")
    private double longitude;

    @NotNull(message = "lattitude must be provided")
    @Max(value = 90, message = "maximum latitude = 90")
    @Min(value = -90, message = "minimum longitude = -90")
    private double lat;
}
