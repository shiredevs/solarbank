package org.solarbank.server;

import org.solarbank.server.CalculateService;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.MethodArgumentNotValidException;


@RestController
@RequestMapping("/api")
public class ServerRestController {

    @Autowired
    private CalculateService calculateService;

    @PostMapping("/calculate")
    public ResponseEntity<String> userInput(@Valid @RequestBody UserInputDto userInputDto) {

        String serviceResponse = calculateService.processUserInput(userInputDto);
        return ResponseEntity.ok(serviceResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {

        StringBuilder errorMessage = new StringBuilder();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errorMessage.append(error.getDefaultMessage()).append("; ");
        });
        return ResponseEntity.badRequest().body(errorMessage.toString());
    }
}
