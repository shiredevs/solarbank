package org.solarbank.server.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.solarbank.server.ValidCurrencyCode;
import org.solarbank.server.ValidationMessage;

@Data
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class EnergyTariff {

    @ValidCurrencyCode
    private String currencyCode;

    @NotNull(message = ValidationMessage.AMOUNT_NULL)
    @DecimalMin(value = "0.01", message = ValidationMessage.AMOUNT_MIN)
    private Double amount;
}
