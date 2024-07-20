package org.solarbank.server;

import jakarta.validation.Valid;
import java.util.Map;
import org.solarbank.server.dto.CalculateRequest;
import org.solarbank.server.dto.CalculateResult;
import org.solarbank.server.service.CalculateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1.0/api")
public class EnergySavingController {

    private final CalculateService calculateService;

    @Autowired
    public EnergySavingController(CalculateService calculateService) {
        this.calculateService = calculateService;
    }

    @PostMapping("/calculate")
    public ResponseEntity<CalculateResult> userInput(
            @Valid @RequestBody CalculateRequest calculateRequest) {
        return ResponseEntity.ok(calculateService.processCalculateRequest(calculateRequest));
    }
}
