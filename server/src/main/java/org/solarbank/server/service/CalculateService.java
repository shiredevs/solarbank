package org.solarbank.server.service;

import java.time.Month;
import java.util.HashMap;
import java.util.Map;
import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import tech.tablesaw.aggregate.AggregateFunctions;
import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.NumericColumn;
import tech.tablesaw.api.StringColumn;
import tech.tablesaw.api.Table;
import org.javamoney.moneta.Money;
import org.solarbank.server.dto.CalculateResult;
import org.solarbank.server.dto.CalculateResult.SavingsPerYear;
import org.solarbank.server.dto.EnergyTariff;
import org.solarbank.server.dto.PanelSize;
import org.solarbank.server.dto.Location;
import org.springframework.stereotype.Service;

@Service
public class CalculateService {
    public CalculateResult processCalculateRequest(
        PanelSize panelSize,
        Double panelEfficiency,
        EnergyTariff energyTariff,
        Map<String, Double> nasaData
    ) {
        Double panelArea = panelSize.getHeight() * panelSize.getWidth();

        Table monthlyAverageTable = calculateEnergyGenPerMonth(nasaData, panelEfficiency, panelArea);

        Map<String, Double> energyGenPerMonth = new HashMap<>();
        for (int i = 0; i < monthlyAverageTable.rowCount(); i++) {
            String month = monthlyAverageTable.stringColumn("Month").get(i);
            double energy = monthlyAverageTable.doubleColumn("Energy").get(i);
            energyGenPerMonth.put(month, energy);
        }

        Double energyGenPerYear = monthlyAverageTable.doubleColumn("Energy").sum();

        Double savings = energyGenPerYear * energyTariff.getAmount();

        SavingsPerYear savingsPerYear = new SavingsPerYear();
        CurrencyUnit currencyUnit = Monetary.getCurrency(energyTariff.getCurrencyCode());
        MonetaryAmount amount = Money.of(savings, currencyUnit);
        savingsPerYear.setCurrencyCode(currencyUnit);
        savingsPerYear.setAmount(amount);

        CalculateResult result = new CalculateResult();
        result.setEnergyGenPerMonth(energyGenPerMonth);
        result.setEnergyGenPerYear(energyGenPerYear);
        result.setSavingsPerYear(savingsPerYear);

        return result;
    }

    public Table calculateEnergyGenPerMonth(
            Map<String, Double> nasaData,
            Double panelEfficiency,
            Double panelArea) {
        StringColumn dateColumn = StringColumn.create("Date");
        DoubleColumn valueColumn = DoubleColumn.create("Value");

        nasaData.forEach((date, value) -> {
            dateColumn.append(date);
            valueColumn.append(value);
        });

        Table table = Table.create("Solar Radiation Data")
                .addColumns(dateColumn, valueColumn);

        table = table.dropWhere(table.stringColumn("Date").map(date -> date.substring(4, 6)).isEqualTo("13"));

        StringColumn monthColumn = table.stringColumn("Date").map(date -> {
            int monthValue = Integer.parseInt(date.substring(4, 6));
            String monthName = Month.of(monthValue).name().toLowerCase();
            return monthName.substring(0, 1).toUpperCase() + monthName.substring(1);
        }).setName("Month");
        table.addColumns(monthColumn);

        Table monthlyAverageTable = table.summarize("Value", AggregateFunctions.mean)
                .by("Month");

        /* Forgot to do Mean * number of days in each month */

        NumericColumn<?> meanColumn = monthlyAverageTable.numberColumn("Mean [Value]");
        DoubleColumn energyColumn = meanColumn.multiply(panelEfficiency).multiply(panelArea).setName("Energy");
        monthlyAverageTable.addColumns(energyColumn);

        return monthlyAverageTable;
    }
}