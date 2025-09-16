package org.solarbank.server.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {
    private CorsProperties cors;
    private clientProperties client;

    @Getter
    @Setter
    public static class CorsProperties {
        private String allowedOrigin;
    }

    @Getter
    @Setter
    public static class clientProperties {
        private int retryMaxAttempts;
        private int retryMaxBackoff;
        private int retryInitialBackoff;
    }
}
