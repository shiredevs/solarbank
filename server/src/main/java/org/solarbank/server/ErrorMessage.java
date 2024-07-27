package org.solarbank.server;

import lombok.Getter;

@Getter
public enum ErrorMessage {
    INTERNAL_SERVER_ERROR("An unexpected error occurred. Please try again later.");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }
}
