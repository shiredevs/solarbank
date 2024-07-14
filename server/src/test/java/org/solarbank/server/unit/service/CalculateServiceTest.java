package org.solarbank.server.unit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.solarbank.server.dto.CalculateRequest;
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
    public void testProcessCalculateRequest() {
        CalculateRequest calculateRequest = new CalculateRequest();
//        Map<String, Object> result = calculateService.processCalculateRequest(calculateRequest);
//
//        assertEquals(1.0, result.get("energyGenPerYear"));
//
//        Map<String, Double> energyGenPerMonth = (Map<String, Double>) result.get("energyGenPerMonth");
//        assertEquals(0.1, energyGenPerMonth.get("January"));
//        assertEquals(0.2, energyGenPerMonth.get("February"));
//
//        Map<String, Object> savingsPerYear = (Map<String, Object>) result.get("savingsPerYear");
//        assertEquals("USD", savingsPerYear.get("currencyCode"));
//        assertEquals(1000.0, savingsPerYear.get("amount"));
    }
}
