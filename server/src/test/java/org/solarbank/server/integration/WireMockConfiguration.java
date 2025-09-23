package org.solarbank.server.integration;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@TestConfiguration
public class WireMockConfiguration {
    @Value("${wiremock.port}")
    private int wiremockPort;

    @Bean
    public WireMockServer wireMockServer() {
        return new WireMockServer(
            wireMockConfig()
                // We are just using http for the outbound calls from the web client in test.
                // This is because we don't need to configure a trust store for the real web client
                // as it's just going to be used to hit the public internet via HTTPS.
                // Web client automatically uses the java default trust store if you don't configure anything.
                // This is enough in this case as the certs are issued by a public authority,
                //
                // so they will be in the default trust store.
                .port(wiremockPort == 0 ? 9999 : wiremockPort)
        );
    }
}
