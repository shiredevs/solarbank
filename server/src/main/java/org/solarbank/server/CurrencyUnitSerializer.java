package org.solarbank.server;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import javax.money.CurrencyUnit;

public class CurrencyUnitSerializer extends JsonSerializer<CurrencyUnit> {

    @Override
    public void serialize(
            CurrencyUnit currencyUnit,
            JsonGenerator jsonGenerator,
            SerializerProvider serializerProvider
    ) throws IOException {
        jsonGenerator.writeString(currencyUnit.getCurrencyCode());
    }
}