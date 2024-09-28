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
    private static final String allSkyParameter = "ALLSKY_SFC_SW_DWN";

    public NasaClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://power.larc.nasa.gov/api/temporal/monthly").build();
    }

    public Map<String, Double> getNasaData(Location location) {
        NasaResponse nasaResponse = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/point")
                        .queryParam("parameters", allSkyParameter)
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

        return nasaResponse.getProperties().getParameter().get(allSkyParameter);
    }
}