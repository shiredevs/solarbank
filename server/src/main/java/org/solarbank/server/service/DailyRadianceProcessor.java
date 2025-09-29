package org.solarbank.server.service;

import java.time.Month;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.Data;
import org.solarbank.server.dto.MeanDailyRadiance;
import org.springframework.stereotype.Service;

@Service
public class DailyRadianceProcessor {

    public Map<Month, TotalMeanDailyRadiance> calculateTotalMeanDailyRadianceByMonth(MeanDailyRadiance meanDailyRadiance) {
        Map<Month, TotalMeanDailyRadiance> result = prePopulateResult();

        if (meanDailyRadiance != null) {
            handleCalculation(meanDailyRadiance.meanDailyRadianceByMonthAndYear(), result);
        }

        return result;
    }

    public double calculateMeanDailyRadianceFor(Month month, TotalMeanDailyRadiance totalMeanDailyRadiance) {
        int  numberOfMonthsRecorded = totalMeanDailyRadiance.getNumberOfMonthsRecorded();
        double dailyRadiance = totalMeanDailyRadiance.getTotalMeanDailyRadiance();
        double result = 0.0;

        if (validInputs(month, dailyRadiance, numberOfMonthsRecorded)) {
            result = dailyRadiance / numberOfMonthsRecorded * month.length(false);
        }

        return result;
    }

    private static boolean validInputs(Month month, double dailyRadiance, int numberOfMonthsRecorded) {
        return month != null
            && numberOfMonthsRecorded > 0
            && dailyRadiance >= 0;
    }

    private Map<Month, TotalMeanDailyRadiance> prePopulateResult() {
        return IntStream.rangeClosed(1, 12)
            .boxed()
            .collect(Collectors.toMap(
                Month::of,
                month -> new TotalMeanDailyRadiance(),
                (oldValue, newValue) -> oldValue,
                LinkedHashMap::new
            ));
    }

    private void handleCalculation(Map<String, Double> radianceByDate, Map<Month, TotalMeanDailyRadiance> result) {
        radianceByDate.forEach((date, radianceValue) -> {
            int monthValue = 0;

            if (isValidString(date)) {
                monthValue = Integer.parseInt(date.substring(date.length() - 2));
            }

            if (isValidEntry(radianceValue, monthValue)) {
                result.computeIfPresent(Month.of(monthValue), updateRadianceWith(radianceValue));
            }
        });
    }

    private static boolean isValidString(String date) {
        return date.length() == 6 && Character.isDigit(date.charAt(4)) && Character.isDigit(date.charAt(5));
    }

    private boolean isValidEntry(Double radianceValue, int monthValue) {
        return radianceValue != null && radianceValue >= 0 && monthValue >= 1 && monthValue <= 12;
    }

    private BiFunction<Month, TotalMeanDailyRadiance, TotalMeanDailyRadiance> updateRadianceWith(double radianceValue) {
        return (month, radiance) -> {
            radiance.setTotalMeanDailyRadiance(radiance.getTotalMeanDailyRadiance() + radianceValue);
            radiance.setNumberOfMonthsRecorded(radiance.getNumberOfMonthsRecorded() + 1);

            return radiance;
        };
    }

    @Data
    public static class TotalMeanDailyRadiance {
        private int numberOfMonthsRecorded;
        private double totalMeanDailyRadiance;
    }
}
