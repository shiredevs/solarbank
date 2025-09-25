package org.solarbank.server.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.BeforeEach;
import org.solarbank.server.dto.CalculateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Import(value={WireMockConfiguration.class})
public class IntegrationTestBase {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected WireMockServer mockServer;

    protected final String FAIL_MESSAGE = "failed to perform test api requests as expected";
    private final ObjectMapper requestMapper = new ObjectMapper();

    @BeforeEach
    public void resetWireMock() {
        mockServer.resetAll();
    }

    public String mapToString(CalculateRequest calculateRequest) throws JsonProcessingException {
        return requestMapper.writeValueAsString(calculateRequest);
    }
}
