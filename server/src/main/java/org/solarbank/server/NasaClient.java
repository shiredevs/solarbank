package org.solarbank.server;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class NasaClient {

    private final WebClient webClient;

    public NasaClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://power.larc.nasa.gov/api/temporal/daily").build();
    }

    public String getNasaData() {
        return webClient.get()
                .uri("/point?parameters=ALLSKY_SFC_SW_DWN&community=RE&longitude=-118.25&latitude=34.05&start=20230101&end=20231231&format=JSON")
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}