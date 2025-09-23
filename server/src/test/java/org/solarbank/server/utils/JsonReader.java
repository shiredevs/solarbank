package org.solarbank.server.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonReader {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T fromClassResource(String path, Class<T> responseType) throws IOException {
        return objectMapper.readValue(
            JsonReader.class.getResourceAsStream(path),
            responseType
        );
    }
}
