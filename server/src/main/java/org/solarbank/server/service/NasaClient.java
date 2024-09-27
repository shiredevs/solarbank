package org.solarbank.server.service;

import java.util.Map;
import java.util.HashMap;
import org.solarbank.server.dto.Location;
import org.solarbank.server.dto.NasaResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class NasaClient {

    private final WebClient webClient;

    public NasaClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://power.larc.nasa.gov/api/temporal/monthly").build();
    }

    public Map<String, Double> calculateMonthlySolarRadiation(Map<String, Double> nasaData) {
// Create a map to hold the total solar radiation and count for each month
        Map<Integer, Double> monthlyTotals = new HashMap<>();
        Map<Integer, Integer> monthlyCounts = new HashMap<>();

        // Iterate over the data
        for (Map.Entry<String, Double> entry : nasaData.entrySet()) {
            String yearMonth = entry.getKey();
            Double value = entry.getValue();

            // Extract the month (last two digits of the key)
            int month = Integer.parseInt(yearMonth.substring(4, 6));

            // Update totals and counts
            monthlyTotals.put(month, monthlyTotals.getOrDefault(month, 0.0) + value);
            monthlyCounts.put(month, monthlyCounts.getOrDefault(month, 0) + 1);
        }

        // Calculate averages
        Map<String, Double> averages = new HashMap<>();
        for (int month = 1; month <= 12; month++) {
            Double total = monthlyTotals.getOrDefault(month, 0.0);
            Integer count = monthlyCounts.getOrDefault(month, 0);
            averages.put(getMonthName(month), count == 0 ? 0.0 : total / count);
        }

        return averages;
    }

    private static String getMonthName(int month) {
        String[] monthNames = {
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        };
        return monthNames[month - 1];
    }

    public Map<String, Double> getNasaData(Location location) {
        NasaResponse nasaResponse = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/point")
                        .queryParam("parameters", "ALLSKY_SFC_SW_DWN")
                        .queryParam("community", "RE")
                        .queryParam("longitude", location.getLongitude())
                        .queryParam("latitude", location.getLatitude())
                        .queryParam("start", "2011")
                        .queryParam("end", "2021")
                        .queryParam("format", "JSON")
                        .build())
                .retrieve()
                .bodyToMono(NasaResponse.class)
                .block();

        Map<String, Double> nasaData = nasaResponse.getProperties().getParameter().get("ALLSKY_SFC_SW_DWN");
        Map<String, Double> monthlyAverage = calculateMonthlySolarRadiation(nasaData);

        return monthlyAverage;
    }
}