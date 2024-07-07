package org.solarbank.server;

import org.solarbank.server.UserInputDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api")
public class ServerRestController {

//    @GetMapping("/")
//    public String index() {
//        return "Greetings from Spring Boot!";
//    }

    @PostMapping("/user-input")
    public String receiveUserInput(@RequestBody UserInputDto userInputDto) {
        // Process the user input as needed
        double longitude = userInputDto.getLocation().getLongitude();
        double latitude = userInputDto.getLocation().getLatitude();
        double height = userInputDto.getPanelSize().getHeight();
        double width = userInputDto.getPanelSize().getWidth();
        double panelEfficiency = userInputDto.getPanelEfficiency();
        String currencyCode = userInputDto.getEnergyTariff().getCurrencyCode();
        double amount = userInputDto.getEnergyTariff().getAmount();

        return String.format("Received user input: Location = (%f, %f), Panel Size = (%f, %f), Panel Efficiency = %f, Energy Tariff = (%s, %f)",
                longitude, latitude, height, width, panelEfficiency, currencyCode, amount);
    }

}