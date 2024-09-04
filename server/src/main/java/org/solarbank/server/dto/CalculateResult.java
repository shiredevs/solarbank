package org.solarbank.server.dto;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.IOException;
import java.util.Map;
import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class CalculateResult {
    private Double energyGenPerYear;
    private Map<String, Double> energyGenPerMonth;
    private SavingsPerYear savingsPerYear;

    @Data
    @JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
    @JsonSerialize(using = SavingsPerYearSerializer.class)
    public static class SavingsPerYear {
        private CurrencyUnit currencyCode;
        private MonetaryAmount amount;
    }

    public static class SavingsPerYearSerializer extends JsonSerializer<SavingsPerYear> {
        @Override
        public void serialize(
            SavingsPerYear savingsPerYear,
            JsonGenerator jsonGenerator,
            SerializerProvider serializerProvider
        ) throws IOException {
            String currencyCode = savingsPerYear.getCurrencyCode().getCurrencyCode();
            double amount = savingsPerYear.getAmount().getNumber().doubleValue();

            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("CurrencyCode", currencyCode);
            jsonGenerator.writeNumberField("Amount", amount);
            jsonGenerator.writeEndObject();
        }
    }
}
