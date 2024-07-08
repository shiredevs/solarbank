package org.solarbank.server;

import jakarta.validation.Valid;
import java.util.Map;
import org.solarbank.server.dto.UserInputDto;
import org.solarbank.server.service.CalculateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ServerRestController {

    @Autowired
    private CalculateService calculateService;

    /**
     * runs calculate service layer, parsing user input data.
     *
     * @param userInputDto  the user input data from the front end
     * @return              returns the ResponseEntity object
     */
    @PostMapping("/calculate")
    public ResponseEntity<Map<String, Object>> userInput(
            @Valid @RequestBody UserInputDto userInputDto) {

        Map<String, Object> result = calculateService.processUserInput(userInputDto);
        return ResponseEntity.ok(result);
    }
}
