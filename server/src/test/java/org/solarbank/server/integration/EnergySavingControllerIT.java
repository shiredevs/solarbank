package org.solarbank.server.integration;

import org.junit.jupiter.api.Test;
import org.solarbank.server.EnergySavingController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class EnergySavingControllerIT extends IntegrationTestBase {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void energySavingController_validInput_status200() throws Exception {
        String userInputJson = "{"
                + "\"Location\": {"
                + "\"Longitude\": 45.0,"
                + "\"Latitude\": 90.0"
                + "},"
                + "\"PanelSize\": {"
                + "\"Height\": 2.0,"
                + "\"Width\": 3.0"
                + "},"
                + "\"PanelEfficiency\": 0.85,"
                + "\"EnergyTariff\": {"
                + "\"CurrencyCode\": \"USD\","
                + "\"Amount\": 0.01"
                + "}"
                + "}";

        mockMvc.perform(post("/v1.0/api/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userInputJson))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.EnergyGenPerYear").value(1.0))
                .andExpect(jsonPath("$.EnergyGenPerMonth.January").value(0.1))
                .andExpect(jsonPath("$.EnergyGenPerMonth.February").value(0.2))
                .andExpect(jsonPath("$.SavingsPerYear.CurrencyCode").value("USD"))
                .andExpect(jsonPath("$.SavingsPerYear.Amount").value(1000.0));
    }

    @Test
    public void energySavingController_invalidInput_status400() throws Exception {
        String userInputJson = "{"
                + "\"Location\": {"
                + "\"Longitude\": 185.0,"
                + "\"Latitude\": 95.0"
                + "},"
                + "\"PanelSize\": {"
                + "\"Height\": -2.0,"
                + "\"Width\": -3.0"
                + "},"
                + "\"PanelEfficiency\": 0.0,"
                + "\"EnergyTariff\": {"
                + "\"CurrencyCode\": \"USD\","
                + "\"Amount\": -0.01"
                + "}"
                + "}";

        mockMvc.perform(post("/v1.0/api/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userInputJson))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.Error.Code").value(400))
                .andExpect(jsonPath("$.Error.Status").value("Bad Request"))
                .andExpect(jsonPath("$.Error.Message").isNotEmpty());
    }
}
