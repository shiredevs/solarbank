package org.solarbank.server;

import java.util.Objects;
import java.util.stream.Collectors;
import org.solarbank.server.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class ControllerExceptionHandler {

    private void log(Exception ex, ErrorResponse errorResponse) {
        String logMessage = String.format(
            "Exception occurred with message: %s; Response to client: %s",
            ex.getMessage(),
            errorResponse.toString()
        );
        System.out.println(logMessage);
        ex.printStackTrace();
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex) {

        String message = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(fieldError -> Objects.requireNonNullElse(
                fieldError.getDefaultMessage(),
                ErrorMessage.NO_ERROR_MESSAGE.getMessage()
            )).collect(Collectors.joining("; "));

        if (message.isEmpty()) {
            message = ErrorMessage.NO_FIELD_ERRORS.getMessage();
        }

        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.getReasonPhrase(),
            message
        );

        log(ex, errorResponse);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> defaultExceptionHandler(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
            ErrorMessage.INTERNAL_SERVER_ERROR.getMessage()
        );
        log(ex, errorResponse);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
        HttpMessageNotReadableException ex
    ) {
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.getReasonPhrase(),
            ValidationMessage.REQUEST_NULL
        );
        log(ex, errorResponse);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFoundException(NoResourceFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ErrorMessage.NOT_FOUND.getMessage() + ex.getResourcePath()
        );
        log(ex, errorResponse);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
