package org.solarbank.server.configuration;

import org.solarbank.server.service.NasaClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NasaClientConfig {

    @Value("${nasa.api.base-url}")
    private String baseUrl;

    @Bean
    public NasaClient nasaClient() {
        return new NasaClient(baseUrl);
    }
}
