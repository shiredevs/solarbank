package org.solarbank.server.unit;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.solarbank.server.EnergySavingController;
import org.solarbank.server.dto.CalculateRequest;
import org.solarbank.server.service.CalculateService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ServerRestControllerTest {

    @Mock
    private CalculateService calculateService;

    @InjectMocks
    private EnergySavingController energyRestController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCalculateRequest() {
        CalculateRequest calculateRequest = new CalculateRequest(); // Set appropriate values

        Map<String, Object> mockResult = new HashMap<>();
        mockResult.put("energyGenPerYear", 1.0);
        mockResult.put("energyGenPerMonth", Map.of("January", 0.1, "February", 0.2));
        mockResult.put("savingsPerYear", Map.of("currencyCode", "USD", "amount", 1000.0));

        when(calculateService.processCalculateRequest(any(CalculateRequest.class))).thenReturn(mockResult);

        ResponseEntity<Map<String, Object>> response = energyRestController.userInput(calculateRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResult, response.getBody());
    }
}
