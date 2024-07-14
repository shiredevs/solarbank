package org.solarbank.server;

public enum Messages {
    INTERNAL_SERVER_ERROR("An unexpected error occurred. Please try again later."),
    CURRENCY("currency not found");

    private final String message;

    Messages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
