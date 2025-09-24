package org.solarbank.server.integration.controller;

import org.junit.jupiter.api.Test;
import org.solarbank.server.error.ErrorMessage;
import org.solarbank.server.integration.IntegrationTestBase;
import org.solarbank.server.validation.ValidationMessage;
import org.solarbank.server.dto.CalculateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EnergySavingControllerIT extends IntegrationTestBase {
    private final static String ACTUAL_URL_FORMAT = "/temporal/monthly/point?start=2005&end=2024&latitude=%f&longitude=%.7f&" +
            "community=re&parameters=ALLSKY_SFC_SW_DWN&format=json&units=metric&header=true&" +
            "time-standard=utc";

    @Test
    public void energySavingController_validRequest_status200() {
        double latitude = 51.493518;
        double longitude = -2.6494647;
        CalculateRequest calculateRequest = createCalculateRequest(latitude, longitude);
        String actualEndpointWithQueryParams = String.format(
            ACTUAL_URL_FORMAT,
            latitude,
            longitude
        );

        mockServer.stubFor(get(urlEqualTo(actualEndpointWithQueryParams))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withStatus(200)
                .withBodyFile("twenty-year-radiance-response.json")));

        try {
            String validRequest = mapToString(calculateRequest);

            mockMvc.perform(post("/v1.0/api/calculate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(validRequest))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.EnergyGenPerYear").value(972.82))
                .andExpect(jsonPath("$.EnergyGenPerMonth.January").value(22.49))
                .andExpect(jsonPath("$.EnergyGenPerMonth.February").value(37.14))
                .andExpect(jsonPath("$.SavingsPerYear.CurrencyCode").value("USD"))
                .andExpect(jsonPath("$.SavingsPerYear.Amount").value(972.82));
        } catch (Exception e) {
            fail(FAIL_MESSAGE);
        }
    }

    @Test
    public void energySavingController_invalidRequest_status400() {
        CalculateRequest calculateRequest = createCalculateRequest();
        calculateRequest.getLocation().setLatitude(190.0);

        try {
            String invalidRequest = mapToString(calculateRequest);

            mockMvc.perform(post("/v1.0/api/calculate")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(invalidRequest))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andExpect(jsonPath("$.Error.Code").value(400))
                    .andExpect(jsonPath("$.Error.Status").value(HttpStatus.BAD_REQUEST.getReasonPhrase()))
                    .andExpect(jsonPath("$.Error.Message").value(ValidationMessage.LATITUDE_MAX));
        } catch (Exception e) {
            fail(FAIL_MESSAGE);
        }
    }

    @Test
    public void energySavingController_emptyRequest_status400() {
        try {
            mockMvc.perform(post("/v1.0/api/calculate")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andExpect(jsonPath("$.Error.Code").value(400))
                    .andExpect(jsonPath("$.Error.Status").value(HttpStatus.BAD_REQUEST.getReasonPhrase()))
                    .andExpect(jsonPath("$.Error.Message").value(ErrorMessage.INVALID_BODY.getMessage()));
        } catch (Exception e) {
            fail(FAIL_MESSAGE);
        }
    }

    @Test
    public void energySavingController_callToNasaPowerFails_status500() {
        CalculateRequest calculateRequest = createCalculateRequest();

        try {
            String validRequest = mapToString(calculateRequest);

            mockMvc.perform(post("/v1.0/api/calculate")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(validRequest))
                    .andExpect(status().isInternalServerError())
                    .andDo(print())
                    .andExpect(jsonPath("$.Error.Code").value(500))
                    .andExpect(jsonPath("$.Error.Status").value(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()))
                    .andExpect(jsonPath("$.Error.Message").value(ErrorMessage.INTERNAL_SERVER_ERROR.getMessage()));
        } catch (Exception e) {
            fail(FAIL_MESSAGE);
        }
    }

    @Test
    public void nonExistentPath_postRequest_returnsNotFound404() {
        try {
            mockMvc.perform(post("/nonexistent-page"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.Error.Code").value(404))
                    .andExpect(jsonPath("$.Error.Status").value(HttpStatus.NOT_FOUND.getReasonPhrase()))
                    .andExpect(jsonPath("$.Error.Message").value(ErrorMessage.NOT_FOUND.getMessage() + "/nonexistent-page"));
        } catch (Exception e) {
            fail(FAIL_MESSAGE);
        }
    }

    @Test
    public void rootPath_postRequest_returnsNotFound404() {
        try {
            mockMvc.perform(post("/"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.Error.Code").value(404))
                    .andExpect(jsonPath("$.Error.Status").value(HttpStatus.NOT_FOUND.getReasonPhrase()))
                    .andExpect(jsonPath("$.Error.Message").value(ErrorMessage.NOT_FOUND.getMessage() + "/"));
        } catch (Exception e) {
            fail(FAIL_MESSAGE);
        }
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
                    .andExpect(jsonPath("$.Error.Message").value(ErrorMessage.INVALID_BODY.getMessage()));
        } catch (Exception e) {
            fail(FAIL_MESSAGE);
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
            fail(FAIL_MESSAGE);
        }
    }
}