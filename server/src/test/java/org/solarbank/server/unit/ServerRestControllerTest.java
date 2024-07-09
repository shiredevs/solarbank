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
import org.solarbank.server.ServerRestController;
import org.solarbank.server.dto.UserInputDto;
import org.solarbank.server.service.CalculateService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ServerRestControllerTest {

    @Mock
    private CalculateService calculateService;

    @InjectMocks
    private ServerRestController serverRestController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUserInput() {
        UserInputDto userInputDto = new UserInputDto(); // Set appropriate values

        Map<String, Object> mockResult = new HashMap<>();
        mockResult.put("energyGenPerYear", 1.0);
        mockResult.put("energyGenPerMonth", Map.of("January", 0.1, "February", 0.2));
        mockResult.put("savingsPerYear", Map.of("currencyCode", "USD", "amount", 1000.0));

        when(calculateService.processUserInput(any(UserInputDto.class))).thenReturn(mockResult);

        ResponseEntity<Map<String, Object>> response = serverRestController.userInput(userInputDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResult, response.getBody());
    }
}
