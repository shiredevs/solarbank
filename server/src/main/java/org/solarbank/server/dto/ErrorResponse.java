package org.solarbank.server.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class ErrorResponse {
    private ErrorDetails error;

    public ErrorResponse(int code, String status, String message) {
        this.error = new ErrorDetails(code, status, message);
    }

    @Data
    public static class ErrorDetails {
        private int code;
        private String status;
        private String message;

        public ErrorDetails(int code, String status, String message) {
            this.code = code;
            this.status = status;
            this.message = message;
        }
    }
}
