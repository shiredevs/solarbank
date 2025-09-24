package org.solarbank.server.controller;

import jakarta.validation.Valid;
import org.solarbank.server.dto.CalculateRequest;
import org.solarbank.server.dto.CalculateResult;
import org.solarbank.server.service.EnergySavingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1.0/api")
public class EnergySavingController {

    private final EnergySavingService energySavingService;

    @Autowired
    public EnergySavingController(EnergySavingService energySavingService) {
        this.energySavingService = energySavingService;
    }

    @PostMapping("/calculate")
    public ResponseEntity<CalculateResult> getSavings(
        @Valid @RequestBody CalculateRequest calculateRequest
    ) {
        return ResponseEntity.ok(energySavingService.calculateSavings(calculateRequest));
    }
}
