package org.solarbank.server.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.money.CurrencyUnit;
import javax.money.Monetary;
import lombok.extern.slf4j.Slf4j;
import org.javamoney.moneta.Money;
import org.solarbank.server.client.NasaPowerClient;
import org.solarbank.server.dto.CalculateRequest;
import org.solarbank.server.dto.CalculateResult;
import org.solarbank.server.dto.CalculateResult.SavingsPerYear;
import org.solarbank.server.dto.EnergyTariff;
import org.solarbank.server.dto.MeanDailyRadiance;
import org.solarbank.server.dto.PanelSize;
import org.solarbank.server.dto.RadianceResponse;
import org.solarbank.server.service.DailyRadianceProcessor.TotalMeanDailyRadiance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EnergySavingService {
    private final DailyRadianceProcessor processor;
    private final NasaPowerClient client;

    @Autowired
    public EnergySavingService(DailyRadianceProcessor processor, NasaPowerClient client) {
        this.processor = processor;
        this.client = client;
    }

    public CalculateResult calculateSavings(CalculateRequest calculateRequest) {
        LinkedHashMap<String, Double> energyGenPerMonth = calculateEnergyGenPerMonth(calculateRequest);
        double energyGenPerYear = calculateEnergyGenPerYear(energyGenPerMonth);
        SavingsPerYear savingsPerYear = calculatesSavingsPerYear(calculateRequest.getEnergyTariff(), energyGenPerYear);

        CalculateResult result = new CalculateResult();
        result.setEnergyGenPerMonth(energyGenPerMonth);
        result.setEnergyGenPerYear(energyGenPerYear);
        result.setSavingsPerYear(savingsPerYear);

        log.info("Calculated savings: {} for request: {}", result, calculateRequest);

        return result;
    }

    private LinkedHashMap<String, Double> calculateEnergyGenPerMonth(CalculateRequest calculateRequest) {
        MeanDailyRadiance meanDailyRadiance = getMeanDailyRadiance(calculateRequest);

        Map<Month, TotalMeanDailyRadiance> totalMeanDailyRadianceByMonth = processor
            .calculateTotalMeanDailyRadianceByMonth(meanDailyRadiance);

        return totalMeanDailyRadianceByMonth
            .entrySet()
            .stream()
            .collect(Collectors.toMap(
                month -> month.getKey().getDisplayName(TextStyle.FULL, Locale.ENGLISH),
                month -> calculateEnergyGenerationFor(
                    month,
                    calculateRequest.getPanelSize(),
                    calculateRequest.getPanelEfficiency()
                ),
                (oldValue, newValue) -> oldValue,
                LinkedHashMap::new
            ));
    }

    private MeanDailyRadiance getMeanDailyRadiance(CalculateRequest calculateRequest) {
        return Optional.ofNullable(client.getMeanDailyRadianceFor(calculateRequest.getLocation()))
            .map(RadianceResponse::getProperties)
            .map(RadianceResponse.Properties::getParameter)
            .map(Map::values)
            .orElse(Collections.emptyList())
            .stream()
            .findFirst()
            .orElseGet(() -> new MeanDailyRadiance(Collections.emptyMap()));
    }

    private double calculateEnergyGenerationFor(
        Map.Entry<Month, TotalMeanDailyRadiance> month,
        PanelSize panelSize,
        double  panelEfficiency
    ) {
        double panelArea = panelSize.getHeight() * panelSize.getWidth();
        double meanDailyRadianceForMonth = processor.calculateMeanDailyRadianceFor(month.getKey(), month.getValue());

        return roundToTwoDp(meanDailyRadianceForMonth * panelArea * panelEfficiency);
    }

    private double calculateEnergyGenPerYear(LinkedHashMap<String, Double> energyGenPerMonth) {
        return roundToTwoDp(energyGenPerMonth
            .values()
            .stream()
            .mapToDouble(Double::doubleValue)
            .sum()
        );
    }

    private SavingsPerYear calculatesSavingsPerYear(EnergyTariff energyTariff, double energyGenPerYear) {
        SavingsPerYear savingsPerYear = new SavingsPerYear();
        CurrencyUnit currencyUnit = Monetary.getCurrency(energyTariff.getCurrencyCode());
        savingsPerYear.setCurrencyCode(currencyUnit);
        double amount = roundToTwoDp(energyGenPerYear * energyTariff.getAmount());
        savingsPerYear.setAmount(Money.of(amount, currencyUnit));

        return savingsPerYear;
    }

    private double roundToTwoDp(double value) {
        return new BigDecimal(value)
            .setScale(2, RoundingMode.HALF_UP)
            .doubleValue();
    }
}
