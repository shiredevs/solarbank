package org.solarbank.server.configuration;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    private final ApplicationProperties.CorsProperties corsProperties;

    @Autowired
    public CorsConfig(ApplicationProperties applicationProperties) {
        this.corsProperties = applicationProperties.getCors();
    }

    @Override
    public void addCorsMappings(@NotNull CorsRegistry registry) {
        String allowedOrigin = corsProperties.getAllowedOrigin();

        if (allowedOrigin == null || allowedOrigin.isEmpty()) {
            throw new IllegalStateException("cors.allowed-origin configuration is missing");
        }

        registry.addMapping("/**")
            .allowedOrigins(allowedOrigin);
    }
}
