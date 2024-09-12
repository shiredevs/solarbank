package org.solarbank.server.unit.configuration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.solarbank.server.configuration.ApplicationProperties;
import org.solarbank.server.configuration.CorsConfig;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class CorsConfigTest {
    private ApplicationProperties applicationProperties;
    private ApplicationProperties.CorsProperties corsProperties;
    @Mock
    private CorsRegistry corsRegistry;
    @Mock
    private CorsRegistration corsRegistration;
    private CorsConfig corsConfig;

    @BeforeEach
    public void setup() {
        applicationProperties = new ApplicationProperties();
        corsProperties = new ApplicationProperties.CorsProperties();
    }

    @Test
    public void corsConfigInitialised_allowedOriginPropertyPresent_allowedOriginSetInRegistry() {
        String allowedOrigin = "https://localhost:3030";
        corsProperties.setAllowedOrigin(allowedOrigin);
        applicationProperties.setCors(corsProperties);
        when(corsRegistry.addMapping("/**")).thenReturn(corsRegistration);

        corsConfig = new CorsConfig(applicationProperties);
        corsConfig.addCorsMappings(corsRegistry);

        verify(corsRegistry.addMapping("/**")).allowedOrigins(allowedOrigin);
    }

    @Test
    public void corsConfigInitialised_allowedOriginPropertyNull_IllegalStateExceptionThrown() {
        corsProperties.setAllowedOrigin(null);
        applicationProperties.setCors(corsProperties);
        corsConfig = new CorsConfig(applicationProperties);

        assertThrows(IllegalStateException.class, () -> corsConfig.addCorsMappings(corsRegistry));
    }

    @Test
    public void corsConfigInitialised_allowedOriginPropertyEmpty_IllegalStateExceptionThrown() {
        corsProperties.setAllowedOrigin("");
        applicationProperties.setCors(corsProperties);
        corsConfig = new CorsConfig(applicationProperties);

        assertThrows(IllegalStateException.class, () -> corsConfig.addCorsMappings(corsRegistry));
    }
}
