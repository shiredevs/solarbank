package org.solarbank.server.client;

public class NasaPowerClientException extends RuntimeException {
    public NasaPowerClientException(String message, Throwable cause) {
        super(
            String.format("%s, caused by %s", message, cause.toString()),
            cause
        );
    }
}
