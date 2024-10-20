package org.solarbank.server;

import lombok.Getter;

@Getter
public enum ErrorMessage {
    INTERNAL_SERVER_ERROR("An unexpected error occurred. Please try again later."),
    NO_FIELD_ERRORS("Field errors not found"),
    NO_ERROR_MESSAGE("no error captured"),
    NOT_FOUND("Page not found: "),
    API_NOT_FOUND("NASA API not found"),
    API_NOT_RESPONDING("NASA API is not responding"),
    SOMETHING_WRONG("Something went wrong: "),
    RETRYING("Retrying... Attempt #"),
    ERROR_OCCURRED("An error has occurred: ");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }
}