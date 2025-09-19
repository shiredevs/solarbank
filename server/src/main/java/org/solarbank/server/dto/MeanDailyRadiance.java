package org.solarbank.server.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Map;

public record MeanDailyRadiance(
    Map<String, Double> meanDailyRadianceByMonthAndYear
) {
    @JsonCreator
    public static MeanDailyRadiance from(Map<String, Double> map) {
        return new MeanDailyRadiance(map);
    }
}
