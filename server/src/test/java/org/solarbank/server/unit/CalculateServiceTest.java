package org.solarbank.server.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.solarbank.server.dto.UserInputDto;
import org.solarbank.server.service.CalculateService;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

public class CalculateServiceTest {

    private CalculateService calculateService;

    @BeforeEach
    public void setUp() {
        calculateService = new CalculateService();
    }

    @Test
    public void testProcessUserInput() {
        UserInputDto userInputDto = new UserInputDto();
        Map<String, Object> result = calculateService.processUserInput(userInputDto);

        assertEquals(1.0, result.get("energyGenPerYear"));

        Map<String, Double> energyGenPerMonth = (Map<String, Double>) result.get("energyGenPerMonth");
        assertEquals(0.1, energyGenPerMonth.get("January"));
        assertEquals(0.2, energyGenPerMonth.get("February"));

        Map<String, Object> savingsPerYear = (Map<String, Object>) result.get("savingsPerYear");
        assertEquals("USD", savingsPerYear.get("currencyCode"));
        assertEquals(1000.0, savingsPerYear.get("amount"));
    }
}
