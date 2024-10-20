package org.solarbank.server.service;

import java.time.Duration;
import java.util.Map;
import org.solarbank.server.ErrorMessage;
import org.solarbank.server.dto.Location;
import org.solarbank.server.dto.NasaResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Service
public class NasaClient {
    private final WebClient webClient;
    private static final String allSkyParameter = "ALLSKY_SFC_SW_DWN";
    private static final int MAX_RETRY_ATTEMPTS = 3;
    private static final int DELAY_MILLIS = 100;
    private static final String URL = "https://power.larc.nasa.gov/api/temporal/monthly";

    public NasaClient(String URL) {
        this.webClient = WebClient.create(URL);
    }

    public Map<String, Double> getNasaData(Location location) {
        NasaResponse nasaResponse = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/point")
                        .queryParam("parameters", allSkyParameter)
                        .queryParam("community", "RE")
                        .queryParam("longitude", location.getLongitude())
                        .queryParam("latitude", location.getLatitude())
                        .queryParam("start", "2012")
                        .queryParam("end", "2021")
                        .queryParam("format", "JSON")
                        .build())
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals,
                        error -> Mono.error(new RuntimeException(ErrorMessage.API_NOT_FOUND.getMessage())))
                .onStatus(HttpStatus.SERVICE_UNAVAILABLE::equals,
                        error -> Mono.error(new RuntimeException(ErrorMessage.API_NOT_RESPONDING.getMessage())))
                .bodyToMono(NasaResponse.class)
                .doOnError(error -> System.out.println(ErrorMessage.ERROR_OCCURRED.getMessage() + error.getMessage()))
                .onErrorResume(
                        e -> Mono.error(new RuntimeException(ErrorMessage.SOMETHING_WRONG.getMessage() + e.getMessage())))
                .retryWhen(Retry.fixedDelay(MAX_RETRY_ATTEMPTS, Duration.ofMillis(DELAY_MILLIS))
                        .doBeforeRetry(retrySignal -> {
                            System.out.println(ErrorMessage.RETRYING.getMessage() + retrySignal.totalRetries());
                        }))
                .block();

        return nasaResponse.getProperties().getParameter().get(allSkyParameter);
    }
}