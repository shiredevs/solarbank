package org.solarbank.server;

import lombok.Getter;

@Getter
public enum ErrorMessage {
    INTERNAL_SERVER_ERROR("An unexpected error occurred. Please try again later."),
    NO_FIELD_ERRORS("Field errors not found"),
    NO_ERROR_MESSAGE("no error captured"),
    NOT_FOUND("Page not found: "),
    TEST_FAIL("failed to perform test api requests as expected");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }
}