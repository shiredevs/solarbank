package org.solarbank.server;

public enum Messages {
    INTERNAL_SERVER_ERROR("An unexpected error occurred. Please try again later."),
    OTHER("this is a test message");

    private final String message;

    Messages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
