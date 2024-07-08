package org.solarbank.server.dto;

import lombok.Data;

@Data
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

        /**
         * error details.
         *
         * @param code      the error code
         * @param status    the error status
         * @param message   the error message
         */
        public ErrorDetails(int code, String status, String message) {
            this.code = code;
            this.status = status;
            this.message = message;
        }
    }
}
