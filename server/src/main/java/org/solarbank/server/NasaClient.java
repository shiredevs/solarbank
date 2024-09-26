package org.solarbank.server;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.solarbank.server.dto.Location;
import org.solarbank.server.dto.NasaResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class NasaClient {

    private final WebClient webClient;

    public NasaClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://power.larc.nasa.gov/api/temporal/daily").build();
    }

    public NasaResponse getNasaData(Location location) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusMonths(12);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String endDateString = endDate.format(formatter);
        String startDateString = startDate.format(formatter);

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/point")
                        .queryParam("parameters", "ALLSKY_SFC_SW_DWN")
                        .queryParam("community", "RE")
                        .queryParam("longitude", location.getLongitude())
                        .queryParam("latitude", location.getLatitude())
                        .queryParam("start", startDateString)
                        .queryParam("end", endDateString)
                        .queryParam("format", "JSON")
                        .build())
                .retrieve()
                .bodyToMono(NasaResponse.class)
                .block();
    }
}