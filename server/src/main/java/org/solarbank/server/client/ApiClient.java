package org.solarbank.server.client;

import org.solarbank.server.configuration.ApplicationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import reactor.util.retry.RetryBackoffSpec;

import java.time.Duration;
import java.util.function.Function;
import java.util.function.Predicate;

@Service
public class ApiClient {
    private final WebClient webClient;

    public ApiClient(ApplicationProperties applicationProperties) {
        ApplicationProperties.clientProperties config = applicationProperties.getClient();
        webClient = WebClient.builder()
            .filter((request, next) ->
                next.exchange(request)
                    .flatMap(toMono())
                    .retryWhen(createRetrySpec(config))
            ).build();
    }

    public <T> T get(String url, Class<T> responseType) {
        return webClient.get()
            .uri(url)
            .retrieve()
            .bodyToMono(responseType)
            .block();
    }

    private RetryBackoffSpec createRetrySpec(ApplicationProperties.clientProperties config) {
        return Retry.backoff(config.getRetryMaxAttempts(), Duration.ofMillis(config.getRetryInitialBackoff()))
            .maxBackoff(Duration.ofSeconds(config.getRetryMaxBackoff()))
            .filter(isRetry())
            .onRetryExhaustedThrow((retryBackoffSpec, lastThrowable) -> lastThrowable.failure());
    }

    private Predicate<Throwable> isRetry() {
        return throwable -> {
            boolean toRetry = false;

            if (throwable instanceof WebClientResponseException ex) {
                int code = ex.getStatusCode().value();
                toRetry = isServerError(code);
            }
            return toRetry;
        };
    }

    private boolean isServerError(int code) {
        return code >= 500 && code < 600;
    }

    private Function<ClientResponse, Mono<? extends ClientResponse>> toMono() {
        return response -> {
            if (response.statusCode().is5xxServerError()) {
                return response.createException().flatMap(Mono::error);
            } else {
                return Mono.just(response);
            }
        };
    }
}
