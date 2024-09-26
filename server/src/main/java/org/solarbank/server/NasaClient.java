package org.solarbank.server;

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
        return webClient.get()
                .uri("/point?parameters=ALLSKY_SFC_SW_DWN&community=RE&longitude={longitude}&latitude={longitude}&start=20230101&end=20231231&format=JSON",
                        location.getLongitude(), location.getLatitude())
                .retrieve()
                .bodyToMono(NasaResponse.class)
                .block();
    }
}