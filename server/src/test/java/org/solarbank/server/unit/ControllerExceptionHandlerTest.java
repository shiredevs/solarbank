package org.solarbank.server.unit;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.solarbank.server.ControllerExceptionHandler;
import org.solarbank.server.ErrorMessage;
import org.solarbank.server.ValidationMessage;
import org.solarbank.server.dto.CalculateRequest;
import org.solarbank.server.dto.ErrorResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ControllerExceptionHandlerTest {

    private ControllerExceptionHandler handler;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private MethodArgumentNotValidException ex;

    @BeforeEach
    public void setUp() {
        handler = new ControllerExceptionHandler();
    }

    @Test
    public void noFieldErrors_validationExceptionHandler_expectedErrorResponse() throws NoSuchMethodException {
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(Collections.emptyList());

        ResponseEntity<ErrorResponse> response = handler.handleValidationException(ex);

        Method method = CalculateRequest.class.getMethod("setPanelEfficiency", Double.class);
        MethodParameter methodParameter = new MethodParameter(method, 0);

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(methodParameter, bindingResult);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse.ErrorDetails errorDetails = response.getBody().getError();

        assertEquals(400, errorDetails.getCode());
        assertEquals(ErrorMessage.BAD_REQUEST.getMessage(), errorDetails.getStatus());
        assertEquals(ErrorMessage.NO_FIELD_ERRORS.getMessage(), errorDetails.getMessage());
    }

    @Test
    public void invalidInput_validationExceptionHandler_expectedErrorResponse() throws NoSuchMethodException {
        FieldError fieldError = new FieldError(
                "CalculateRequest",
                "panelEfficiency",
                ValidationMessage.PANEL_EFF_POSITIVE
        );

        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        Method method = CalculateRequest.class.getMethod("setPanelEfficiency", Double.class);
        MethodParameter methodParameter = new MethodParameter(method, 0);

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(methodParameter, bindingResult);

        ResponseEntity<ErrorResponse> response = handler.handleValidationException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse.ErrorDetails errorDetails = response.getBody().getError();

        assertEquals(400, errorDetails.getCode());
        assertEquals(ErrorMessage.BAD_REQUEST.getMessage(), errorDetails.getStatus());
        assertEquals(ValidationMessage.PANEL_EFF_POSITIVE, errorDetails.getMessage());
    }

    @Test
    public void invalidInput_validationExceptionHandlerNullMessage_expectedErrorResponse() throws NoSuchMethodException {
        FieldError fieldError = new FieldError(
                "CalculateRequest",
                "panelEfficiency",
                null
        );

        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        Method method = CalculateRequest.class.getMethod("setPanelEfficiency", Double.class);
        MethodParameter methodParameter = new MethodParameter(method, 0);

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(methodParameter, bindingResult);

        ResponseEntity<ErrorResponse> response = handler.handleValidationException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse.ErrorDetails errorDetails = response.getBody().getError();

        assertEquals(400, errorDetails.getCode());
        assertEquals(ErrorMessage.BAD_REQUEST.getMessage(), errorDetails.getStatus());
        assertEquals(ErrorMessage.NO_ERROR_MESSAGE.getMessage(), errorDetails.getMessage());
    }

    @Test
    public void invalidInputMulitple_validationExceptionHandler_expectedErrorResponse() throws NoSuchMethodException {
        List<FieldError> fieldErrors = new ArrayList<>();

        FieldError panelEfficiencyError = new FieldError(
                "CalculateRequest",
                "panelEfficiency",
                ValidationMessage.PANEL_EFF_POSITIVE
        );

        FieldError locationError = new FieldError(
                "CalculateRequest",
                "location",
                ValidationMessage.LOCATION_NULL
        );

        fieldErrors.add(panelEfficiencyError);
        fieldErrors.add(locationError);

        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);

        Method method = CalculateRequest.class.getMethod("setPanelEfficiency", Double.class);
        MethodParameter methodParameter = new MethodParameter(method, 0);

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(methodParameter, bindingResult);

        ResponseEntity<ErrorResponse> response = handler.handleValidationException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse.ErrorDetails errorDetails = response.getBody().getError();

        assertEquals(400, errorDetails.getCode());
        assertEquals(ErrorMessage.BAD_REQUEST.getMessage(), errorDetails.getStatus());
        assertEquals(
                ValidationMessage.PANEL_EFF_POSITIVE + "; " + ValidationMessage.LOCATION_NULL ,
                errorDetails.getMessage()
        );
    }

    @Test
    public void exception_defaultExceptionHandler_expectedServerError() {
        Exception exception = new Exception();

        ResponseEntity<ErrorResponse> response = handler.defaultExceptionHandler(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        ErrorResponse.ErrorDetails errorDetails = response.getBody().getError();

        assertEquals(500, errorDetails.getCode());
        assertEquals(ErrorMessage.INTERNAL_SERVER_ERROR.getMessage(), errorDetails.getStatus());
        assertEquals(ErrorMessage.INTERNAL_SERVER_ERROR_DETAILS.getMessage(), errorDetails.getMessage());
    }

    @Test
    public void messageNotReadableException_messageNotReadableExceptionHandler_expectedServerError() {
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("Invalid request body");

        ResponseEntity<ErrorResponse> response = handler.handleHttpMessageNotReadableException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse.ErrorDetails errorDetails = response.getBody().getError();

        assertEquals(HttpStatus.BAD_REQUEST.value(), errorDetails.getCode());
        assertEquals(HttpStatus.BAD_REQUEST.getReasonPhrase(), errorDetails.getStatus());
        assertEquals(ValidationMessage.REQUEST_NULL, errorDetails.getMessage());
    }
}