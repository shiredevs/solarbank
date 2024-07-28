package org.solarbank.server;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import javax.money.CurrencyUnit;
import java.io.IOException;

public class CurrencyUnitSerializer extends JsonSerializer<CurrencyUnit> {

    @Override
    public void serialize(CurrencyUnit currencyUnit, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(currencyUnit.getCurrencyCode());
    }
}