package org.solarbank.server.integration;

import org.junit.jupiter.api.Test;
import org.solarbank.server.ErrorMessage;
import org.solarbank.server.ValidationMessage;
import org.solarbank.server.dto.CalculateRequest;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EnergySavingControllerIT extends IntegrationTestBase {

    @Test
    public void energySavingController_validRequest_status200() throws Exception {
        CalculateRequest calculateRequest = createCalculateRequest();

        String validRequest = mapToString(calculateRequest);

        mockMvc.perform(post("/v1.0/api/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRequest))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.EnergyGenPerYear").value(1.0))
                .andExpect(jsonPath("$.EnergyGenPerMonth.January").value(0.1))
                .andExpect(jsonPath("$.EnergyGenPerMonth.February").value(0.2))
                .andExpect(jsonPath("$.SavingsPerYear.CurrencyCode").value("USD"))
                .andExpect(jsonPath("$.SavingsPerYear.Amount").value(1000.0));
    }

    @Test
    public void energySavingController_invalidRequest_status400() throws Exception {
        CalculateRequest calculateRequest = createCalculateRequest();

        calculateRequest.getLocation().setLatitude(190.0);

        String invalidRequest = mapToString(calculateRequest);

        mockMvc.perform(post("/v1.0/api/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.Error.Code").value(400))
                .andExpect(jsonPath("$.Error.Status").value(ErrorMessage.BAD_REQUEST.getMessage()))
                .andExpect(jsonPath("$.Error.Message").value(ValidationMessage.LATITUDE_MAX));
    }

    @Test
    public void energySavingController_emptyRequest_status400() throws Exception {
        mockMvc.perform(post("/v1.0/api/calculate")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.Error.Code").value(400))
                .andExpect(jsonPath("$.Error.Status").value(ErrorMessage.BAD_REQUEST.getMessage()))
                .andExpect(jsonPath("$.Error.Message").value(ValidationMessage.REQUEST_NULL));
    }
}