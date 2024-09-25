package org.solarbank.server.service;

import java.util.HashMap;
import java.util.Map;
import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import org.javamoney.moneta.Money;
import org.solarbank.server.dto.CalculateResult;
import org.solarbank.server.dto.CalculateResult.SavingsPerYear;
import org.solarbank.server.dto.EnergyTariff;
import org.solarbank.server.dto.PanelSize;
import org.springframework.stereotype.Service;

import org.springframework.web.reactive.function.client.WebClient;

@Service
public class CalculateService {

    @Autowired
    private WebClient nasaClient;

    public CalculateResult processCalculateRequest(
        PanelSize panelSize,
        Double panelEfficiency,
        EnergyTariff energyTariff
    ) {

        String url = "https://power.larc.nasa.gov/api/temporal/daily/point?parameters=ALLSKY_SFC_SW_DWN&community=RE&longitude=-118.25&latitude=34.05&start=20230101&end=20231231&format=JSON";

        WebClient.Builder nasaClient = WebClient.builder();

        String nasaResponse = nasaClient.build()
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        System.out.println("----------------------------------");
        System.out.println(nasaResponse);
        System.out.println("----------------------------------");

        Map<String, Double> energyGenPerMonth = new HashMap<>();
        energyGenPerMonth.put("January", 0.1);
        energyGenPerMonth.put("February", 0.2);

        SavingsPerYear savingsPerYear = new SavingsPerYear();
        CurrencyUnit currencyUnit = Monetary.getCurrency(energyTariff.getCurrencyCode());
        MonetaryAmount amount = Money.of(1000, currencyUnit);
        savingsPerYear.setCurrencyCode(currencyUnit);
        savingsPerYear.setAmount(amount);

        Double energyGenPerYear = 1.0;

        CalculateResult result = new CalculateResult();
        result.setEnergyGenPerMonth(energyGenPerMonth);
        result.setEnergyGenPerYear(energyGenPerYear);
        result.setSavingsPerYear(savingsPerYear);

        return result;
    }
}
