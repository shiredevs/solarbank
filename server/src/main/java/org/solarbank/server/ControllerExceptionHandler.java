package org.solarbank.server;

import java.util.List;
import org.solarbank.server.ErrorMessage;
import org.solarbank.server.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ControllerExceptionHandler {

    private void log(Exception ex) {
        ex.printStackTrace();
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex) {

        StringBuilder message = new StringBuilder();
        BindingResult bindingResult = null;

        try {
            bindingResult = ex.getBindingResult();
        } catch (Exception e) {
            message.append(ErrorMessage.NO_BINDING_RESULT.getMessage());
        }

        if (bindingResult != null) {
            try {
                List<FieldError> fieldErrors = bindingResult.getFieldErrors();
                if (fieldErrors.isEmpty()) {
                    message.append(ErrorMessage.NO_FIELD_ERRORS.getMessage());
                } else {
                    fieldErrors.forEach(error -> {
                        message.append(error.getDefaultMessage()).append("; ");
                    });
                }
            } catch (Exception e) {
                message.append(ErrorMessage.NO_FIELD_ERRORS.getMessage());
            }
        }

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                message.toString()
        );

        log(ex);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> defaultExceptionHandler(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                ErrorMessage.INTERNAL_SERVER_ERROR_DETAILS.getMessage()
        );
        log(ex);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}