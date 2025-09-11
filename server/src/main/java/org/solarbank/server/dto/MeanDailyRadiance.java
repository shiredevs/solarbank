package org.solarbank.server.dto;

import java.util.Map;

public record MeanDailyRadiance(
    Map<String, Double> meanDailyRadianceByMonthAndYear
) {
}
