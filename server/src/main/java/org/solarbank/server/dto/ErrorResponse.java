package org.solarbank.server.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class ErrorResponse {
    private ErrorDetails error;

    public ErrorResponse(int code, String status, String message) {
        this.error = new ErrorDetails(code, status, message);
    }

    @Data
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
    public static class ErrorDetails {
        private int code;
        private String status;
        private String message;
    }
}
