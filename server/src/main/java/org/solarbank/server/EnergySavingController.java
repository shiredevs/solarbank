package org.solarbank.server;

import jakarta.validation.Valid;
import org.solarbank.server.dto.CalculateRequest;
import org.solarbank.server.dto.CalculateResult;
import org.solarbank.server.dto.NasaResponse;
import org.solarbank.server.service.CalculateService;
import org.solarbank.server.service.NasaClient;
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
    private final NasaClient nasaClient;

    @Autowired
    public EnergySavingController(CalculateService calculateService, NasaClient nasaClient) {
        this.calculateService = calculateService;
        this.nasaClient = nasaClient;
    }

    @PostMapping("/calculate")
    public ResponseEntity<CalculateResult> userInput(
        @Valid @RequestBody CalculateRequest calculateRequest
    ) {

        NasaResponse nasaResponse = nasaClient.getNasaData(calculateRequest.getLocation());

        System.out.println("----------------------------------");
        System.out.println(nasaResponse);
        System.out.println("----------------------------------");

        return ResponseEntity.ok(calculateService.processCalculateRequest(
                calculateRequest.getPanelSize(),
                calculateRequest.getPanelEfficiency(),
                calculateRequest.getEnergyTariff()
        ));
    }
}
