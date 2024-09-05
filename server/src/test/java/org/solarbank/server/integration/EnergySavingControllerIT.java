package org.solarbank.server.integration;

import org.junit.jupiter.api.Test;
import org.solarbank.server.ErrorMessage;
import org.solarbank.server.ValidationMessage;
import org.solarbank.server.dto.CalculateRequest;
import org.solarbank.server.service.CalculateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.boot.test.mock.mockito.SpyBean;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EnergySavingControllerIT extends IntegrationTestBase {

    @SpyBean
    private CalculateService calculateService;

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
            .andExpect(jsonPath("$.Error.Status").value(HttpStatus.BAD_REQUEST.getReasonPhrase()))
            .andExpect(jsonPath("$.Error.Message").value(ValidationMessage.LATITUDE_MAX));
    }

    @Test
    public void energySavingController_emptyRequest_status400() throws Exception {
        mockMvc.perform(post("/v1.0/api/calculate")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andDo(print())
            .andExpect(jsonPath("$.Error.Code").value(400))
            .andExpect(jsonPath("$.Error.Status").value(HttpStatus.BAD_REQUEST.getReasonPhrase()))
            .andExpect(jsonPath("$.Error.Message").value(ValidationMessage.REQUEST_NULL));
    }

    @Test
    public void energySavingController_throwException_status500() throws Exception {
        CalculateRequest calculateRequest = createCalculateRequest();
        String validRequest = mapToString(calculateRequest);

        when(calculateService.processCalculateRequest(
                calculateRequest.getPanelSize(),
                calculateRequest.getPanelEfficiency(),
                calculateRequest.getEnergyTariff()
        )).thenThrow(new RuntimeException());

        mockMvc.perform(post("/v1.0/api/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validRequest))
            .andExpect(status().isInternalServerError())
            .andDo(print())
            .andExpect(jsonPath("$.Error.Code").value(500))
            .andExpect(jsonPath("$.Error.Status").value(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()))
            .andExpect(jsonPath("$.Error.Message").value(ErrorMessage.INTERNAL_SERVER_ERROR.getMessage()));
    }

    @Test
    public void nonExistentPath_postRequest_returnsNotFound404() throws Exception  {
        mockMvc.perform(post("/nonexistent-page"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.Error.Code").value(404))
                .andExpect(jsonPath("$.Error.Status").value(HttpStatus.NOT_FOUND.getReasonPhrase()))
                .andExpect(jsonPath("$.Error.Message").value(ErrorMessage.NOT_FOUND.getMessage() + "nonexistent-page"));
    }

    @Test
    public void emptyLatitude_postRequest_returnsNullRequest400() {
        String invalidRequestBody = """
                {
                  "Location": {
                    "Longitude": 180,
                    "Latitude":
                  },
                  "PanelSize": {
                    "Height": 20,
                    "Width": 20
                  },
                  "PanelEfficiency": 1.0,
                  "EnergyTariff": {
                    "CurrencyCode": "GBP",
                    "Amount": 120
                  }
                }
                """;
        try {
            mockMvc.perform(post("/v1.0/api/calculate")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(invalidRequestBody))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andExpect(jsonPath("$.Error.Code").value(400))
                    .andExpect(jsonPath("$.Error.Status").value(HttpStatus.BAD_REQUEST.getReasonPhrase()))
                    .andExpect(jsonPath("$.Error.Message").value(ValidationMessage.REQUEST_NULL));
        } catch (Exception e) {
            fail("failed to perform test api requests as expected");
        }
    }

    @Test
    public void lowerCaseLocation_postRequest_returnsNullRequest400() {
        String invalidRequestBody = """
                {
                  "location": {
                    "Longitude": 180,
                    "Latitude": 90
                  },
                  "PanelSize": {
                    "Height": 20,
                    "Width": 20
                  },
                  "PanelEfficiency": 1.0,
                  "EnergyTariff": {
                    "CurrencyCode": "GBP",
                    "Amount": 120
                  }
                }
                """;

        try {
            mockMvc.perform(post("/v1.0/api/calculate")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(invalidRequestBody))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andExpect(jsonPath("$.Error.Code").value(400))
                    .andExpect(jsonPath("$.Error.Status").value(HttpStatus.BAD_REQUEST.getReasonPhrase()))
                    .andExpect(jsonPath("$.Error.Message").value(ValidationMessage.LOCATION_NULL));
        } catch (Exception e) {
            fail("failed to perform test api requests as expected");
        }
    }
}