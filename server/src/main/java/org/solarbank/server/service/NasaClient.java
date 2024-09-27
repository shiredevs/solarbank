package org.solarbank.server.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    public NasaResponse getNasaData(Location location) {
        return webClient.get()
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
    }
}