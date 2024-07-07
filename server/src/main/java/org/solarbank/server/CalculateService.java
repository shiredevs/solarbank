package org.solarbank.server;

import org.solarbank.server.UserInputDto;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CalculateService {

    public String processUserInput(UserInputDto userInputDto) {
        return "working";
    }
}


