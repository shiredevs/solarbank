package org.solarbank.server.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.solarbank.server.ValidationMessage;

@Data
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class Location {
    @NotNull(message = ValidationMessage.LONGITUDE_NULL)
    @Max(value = 180, message = ValidationMessage.LONGITUDE_MAX)
    @Min(value = -180, message = ValidationMessage.LONGITUDE_MIN)
    private Double longitude;

    @NotNull(message = ValidationMessage.LATITUDE_NULL)
    @Max(value = 90, message = ValidationMessage.LATITUDE_MAX)
    @Min(value = -90, message = ValidationMessage.LATITUDE_MIN)
    private Double latitude;
}
