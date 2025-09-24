package org.solarbank.server.unit.service;

import org.junit.jupiter.api.Test;
import org.solarbank.server.dto.MeanDailyRadiance;
import org.solarbank.server.service.DailyRadianceProcessor;

import java.time.Month;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.solarbank.server.service.DailyRadianceProcessor.*;

public class DailyRadianceProcessorTest {
    private final DailyRadianceProcessor processor = new DailyRadianceProcessor();

    @Test
    public void calculateTotalRadiance_noDataProvided_returnsPrePopulatedEmptyResult() {
        Map<Month, TotalMeanDailyRadiance> result =
            processor.calculateTotalMeanDailyRadianceByMonth(new MeanDailyRadiance(Map.of()));

        IntStream.rangeClosed(1, 12)
            .forEach(i -> {
                Month month = Month.of(i);
                assertTrue(result.containsKey(month));
                TotalMeanDailyRadiance actualMonth = result.get(month);
                assertNotNull(actualMonth);
                assertInstanceOf(TotalMeanDailyRadiance.class, actualMonth);
                assertEquals(0, actualMonth.getNumberOfMonthsRecorded());
                assertEquals(0.0, actualMonth.getTotalMeanDailyRadiance());
            });
    }

    @Test
    public void calculateTotalRadiance_oneYearOfData_returnsExpectedResult() {
        int testYear = 2024;
        MeanDailyRadiance inputData = generate(testYear, testYear);

        Map<Month, TotalMeanDailyRadiance> result = processor.calculateTotalMeanDailyRadianceByMonth(inputData);

        Map<String, Double> actualRadianceByMonth = inputData.meanDailyRadianceByMonthAndYear();
        result.forEach((month, monthlyRadiance) -> {
            assertEquals(1, monthlyRadiance.getNumberOfMonthsRecorded());
            assertEquals(
                actualRadianceByMonth.get(formatKey(testYear, month.getValue())),
                monthlyRadiance.getTotalMeanDailyRadiance()
            );
        });
    }

    @Test
    public void calculateTotalRadiance_20YearsOfData_returnsExpectedResult() {
        int endYear = 2024;
        int startYear = endYear - 19; // as end year is inclusive
        int expectedNumberOfMonths = endYear - startYear + 1;
        MeanDailyRadiance inputData = generate(startYear, endYear);

        Map<Month, TotalMeanDailyRadiance> result = processor.calculateTotalMeanDailyRadianceByMonth(inputData);

        Map<String, Double> actualRadianceByMonth = inputData.meanDailyRadianceByMonthAndYear();
        result.forEach((month, monthlyRadiance) -> {
            assertEquals(expectedNumberOfMonths, monthlyRadiance.getNumberOfMonthsRecorded());
            double sum = IntStream.rangeClosed(startYear, endYear)
                .mapToDouble(year -> actualRadianceByMonth.get(formatKey(year, month.getValue())))
                .sum();
            assertEquals(sum, monthlyRadiance.getTotalMeanDailyRadiance(), 0.00001);
        });
    }

    @Test
    public void calculateTotalRadiance_2YearsOfDataWithMissingMonths_returnsExpectedResult() {
        int endYear = 2024;
        int startYear = endYear - 1; // as end year is inclusive
        int expectedNumberOfMonths = endYear - startYear + 1;
        MeanDailyRadiance inputData = generate(startYear, endYear);
        inputData.meanDailyRadianceByMonthAndYear().remove(formatKey(startYear, 1));
        inputData.meanDailyRadianceByMonthAndYear().remove(formatKey(endYear, 5));

        Map<Month, TotalMeanDailyRadiance> result = processor.calculateTotalMeanDailyRadianceByMonth(inputData);

        result.forEach((month, monthlyRadiance) -> {
            int monthValue = month.getValue();
            if (monthValue == 1 || monthValue == 5) {
                assertEquals(1, monthlyRadiance.getNumberOfMonthsRecorded());
            } else {
                assertEquals(expectedNumberOfMonths, monthlyRadiance.getNumberOfMonthsRecorded());
           }
        });
    }
    @Test
    public void calculateTotalRadiance_oneYearOfDataContainingInvalidValues_returnsExpectedResult() {
        int testYear = 2024;
        int nullMonth = 1;
        int negativeMonth = 12;
        MeanDailyRadiance inputData = generate(testYear, testYear);
        inputData.meanDailyRadianceByMonthAndYear().put(formatKey(testYear, nullMonth), null);
        inputData.meanDailyRadianceByMonthAndYear().put(formatKey(testYear, negativeMonth), -999.0);

        Map<Month, TotalMeanDailyRadiance> result = processor.calculateTotalMeanDailyRadianceByMonth(inputData);

        Map<String, Double> actualRadianceByMonth = inputData.meanDailyRadianceByMonthAndYear();
        result.forEach((month, monthlyRadiance) -> {
            Integer numberOfMonthsRecorded = monthlyRadiance.getNumberOfMonthsRecorded();
            Double totalMeanDailyRadiance = monthlyRadiance.getTotalMeanDailyRadiance();
            int monthValue = month.getValue();

            if (monthValue == nullMonth || monthValue == negativeMonth) {
                assertEquals(0, numberOfMonthsRecorded);
                assertEquals(0.0, totalMeanDailyRadiance);
            } else {
                assertEquals(1, numberOfMonthsRecorded);
                assertEquals(
                    actualRadianceByMonth.get(formatKey(testYear, monthValue)),
                    totalMeanDailyRadiance
                );
            }
        });
    }

    @Test
    public void calculateTotalRadiance_oneYearOfDataContainingZeroValue_correctlyCountsRecordsForThatMonth() {
        int testYear = 2024;
        int monthWithZeroRadianceValue = 1;
        MeanDailyRadiance inputData = generate(testYear, testYear);
        inputData.meanDailyRadianceByMonthAndYear().put(formatKey(testYear, monthWithZeroRadianceValue), 0.0);

        Map<Month, TotalMeanDailyRadiance> result = processor.calculateTotalMeanDailyRadianceByMonth(inputData);

        TotalMeanDailyRadiance actualResult = result.get(Month.of(monthWithZeroRadianceValue));
        assertEquals(0.0, actualResult.getTotalMeanDailyRadiance());
        assertEquals(1, actualResult.getNumberOfMonthsRecorded());
    }

    @Test
    public void calculateTotalRadiance_oneYearOfDataContainingMonthWithNonDigitKey_correctlySkipsThatMonth() {
        int testYear = 2024;
        int monthWithNonDigitKey = 5;
        MeanDailyRadiance inputData = generate(testYear, testYear);
        inputData.meanDailyRadianceByMonthAndYear().remove(formatKey(testYear, monthWithNonDigitKey));
        inputData.meanDailyRadianceByMonthAndYear().put("2024-5", 4.0);

        Map<Month, TotalMeanDailyRadiance> result = processor.calculateTotalMeanDailyRadianceByMonth(inputData);

        TotalMeanDailyRadiance actualResult = result.get(Month.of(monthWithNonDigitKey));
        assertEquals(0.0, actualResult.getTotalMeanDailyRadiance());
        assertEquals(0, actualResult.getNumberOfMonthsRecorded());
    }

    @Test
    public void calculateTotalRadiance_oneYearOfDataContainingMonthWithKeyLengthLessThanSix_correctlySkipsThatMonth() {
        int testYear = 2024;
        int monthWithKeyLengthLessThanSix = 5;

        MeanDailyRadiance inputData = generate(testYear, testYear);
        inputData.meanDailyRadianceByMonthAndYear().remove(formatKey(testYear, monthWithKeyLengthLessThanSix));
        inputData.meanDailyRadianceByMonthAndYear().put("2024", 4.0);

        Map<Month, TotalMeanDailyRadiance> result = processor.calculateTotalMeanDailyRadianceByMonth(inputData);

        TotalMeanDailyRadiance actualResult = result.get(Month.of(monthWithKeyLengthLessThanSix));
        assertEquals(0.0, actualResult.getTotalMeanDailyRadiance());
        assertEquals(0, actualResult.getNumberOfMonthsRecorded());
    }
    @Test
    public void calculateTotalRadiance_inputDataNull_returnsExpectedResult() {
        MeanDailyRadiance inputData = null;

        Map<Month, TotalMeanDailyRadiance> result = processor.calculateTotalMeanDailyRadianceByMonth(inputData);

        result.forEach((month, monthlyRadiance) -> {
            assertEquals(0, monthlyRadiance.getNumberOfMonthsRecorded());
            assertEquals(0.0, monthlyRadiance.getTotalMeanDailyRadiance());
        });
    }

    @Test
    public void calculateMeanDailyRadiance_validDataInputs_returnsCorrectResult() {
        double radianceValue = 7.0;
        int numberOfMonthsRecorded = 2;
        Month january = Month.JANUARY;
        double expectedMeanDailyRadiance = radianceValue / numberOfMonthsRecorded * january.length(false);
        TotalMeanDailyRadiance totalMeanDailyRadiance = new TotalMeanDailyRadiance();
        totalMeanDailyRadiance.setNumberOfMonthsRecorded(numberOfMonthsRecorded);
        totalMeanDailyRadiance.setTotalMeanDailyRadiance(radianceValue);

        Double actualMeanDailyRadiance = processor.calculateMeanDailyRadianceFor(january, totalMeanDailyRadiance);

        assertEquals(expectedMeanDailyRadiance, actualMeanDailyRadiance);
    }

    @Test
    public void calculateMeanDailyRadiance_noMonthsRecorded_returnsCorrectResult() {
        double expectedMeanDailyRadiance = 0.0;
        TotalMeanDailyRadiance totalMeanDailyRadiance = new TotalMeanDailyRadiance();
        Month january = Month.JANUARY;

        Double actualMeanDailyRadiance = processor.calculateMeanDailyRadianceFor(january, totalMeanDailyRadiance);

        assertEquals(expectedMeanDailyRadiance, actualMeanDailyRadiance);
    }

    @Test
    public void calculateMeanDailyRadiance_radianceValuesAllZero_returnsCorrectResult() {
        double radianceValue = 0;
        int numberOfMonthsRecorded = 10;
        double expectedMeanDailyRadiance = 0.0;
        TotalMeanDailyRadiance totalMeanDailyRadiance = new TotalMeanDailyRadiance();
        totalMeanDailyRadiance.setNumberOfMonthsRecorded(numberOfMonthsRecorded);
        totalMeanDailyRadiance.setTotalMeanDailyRadiance(radianceValue);
        Month january = Month.JANUARY;

        Double actualMeanDailyRadiance = processor.calculateMeanDailyRadianceFor(january, totalMeanDailyRadiance);

        assertEquals(expectedMeanDailyRadiance, actualMeanDailyRadiance);
    }

    @Test
    public void calculateMeanDailyRadiance_negativeRadianceValue_returnsCorrectResult() {
        double radianceValue = -7.0;
        int numberOfMonthsRecorded = 2;
        Month january = Month.JANUARY;
        double expectedMeanDailyRadiance = 0.0;
        TotalMeanDailyRadiance totalMeanDailyRadiance = new TotalMeanDailyRadiance();
        totalMeanDailyRadiance.setNumberOfMonthsRecorded(numberOfMonthsRecorded);
        totalMeanDailyRadiance.setTotalMeanDailyRadiance(radianceValue);

        Double actualMeanDailyRadiance = processor.calculateMeanDailyRadianceFor(january, totalMeanDailyRadiance);

        assertEquals(expectedMeanDailyRadiance, actualMeanDailyRadiance);
    }

    @Test
    public void calculateMeanDailyRadiance_nullMonth_returnsCorrectResult() {
        double radianceValue = 7.0;
        int numberOfMonthsRecorded = 2;
        double expectedMeanDailyRadiance = 0.0;
        TotalMeanDailyRadiance totalMeanDailyRadiance = new TotalMeanDailyRadiance();
        totalMeanDailyRadiance.setNumberOfMonthsRecorded(numberOfMonthsRecorded);
        totalMeanDailyRadiance.setTotalMeanDailyRadiance(radianceValue);

        Double actualMeanDailyRadiance = processor.calculateMeanDailyRadianceFor(null, totalMeanDailyRadiance);

        assertEquals(expectedMeanDailyRadiance, actualMeanDailyRadiance);
    }

    private MeanDailyRadiance generate(int start, int end) {
        Map<String, Double> values = new LinkedHashMap<>();

        for (int year = start; year <= end; year++) {
            for (int month = 1; month <= 13; month++) {
                double value = ThreadLocalRandom.current().nextDouble(1.0, 10.0);
                values.put(formatKey(year, month), value);
            }
        }

        return new MeanDailyRadiance(values);
    }

    private String formatKey(int year, int month) {
        return String.format("%d%02d", year, month);
    }
}