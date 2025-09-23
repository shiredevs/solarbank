package org.solarbank.server.dto;

import java.util.Map;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RadianceResponse {
    private Properties properties;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Properties {
        private Map<String, MeanDailyRadiance> parameter;
    }
}
