package org.solarbank.server;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import javax.money.MonetaryAmount;

public class MonetaryAmountSerializer extends JsonSerializer<MonetaryAmount> {

    @Override
    public void serialize(
            MonetaryAmount monetaryAmount,
            JsonGenerator jsonGenerator,
            SerializerProvider serializerProvider
    ) throws IOException {
        jsonGenerator.writeNumber(monetaryAmount.getNumber().doubleValue());
    }
}