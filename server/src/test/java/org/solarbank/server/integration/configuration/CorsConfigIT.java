package org.solarbank.server.integration.configuration;

import org.junit.jupiter.api.Test;
import org.solarbank.server.configuration.ApplicationProperties;
import org.solarbank.server.integration.IntegrationTestBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.fail;
import static org.solarbank.server.utils.RequestHelper.createCalculateRequest;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CorsConfigIT extends IntegrationTestBase {
    @Autowired
    ApplicationProperties applicationProperties;

    @Test
    public void calculateRequestFromValidOrigin_corsEnabled_corsHeaderOnResponse() {
        try {
            String allowedOrigin = applicationProperties.getCors().getAllowedOrigin();

            mockMvc.perform(post("/v1.0/api/calculate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Origin", allowedOrigin)
                    .content(mapToString(createCalculateRequest()))
            )
                .andExpect(header().string("Access-Control-Allow-Origin", allowedOrigin));
        } catch (Exception e) {
            fail(FAIL_MESSAGE);
        }
    }

    @Test
    public void calculateRequestFromInvalidOrigin_corsEnabled_forbiddenResponseStatusReturned() {
        try {
            mockMvc.perform(post("/v1.0/api/calculate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Origin", "https://invalid-origin:3030")
                    .content(mapToString(createCalculateRequest()))
                )
                .andExpect(status().isForbidden())
                .andExpect(content().string("Invalid CORS request"));
        } catch (Exception e) {
            fail(FAIL_MESSAGE);
        }
    }
}
