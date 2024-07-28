package org.solarbank.server.unit;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.solarbank.server.ControllerExceptionHandler;
import org.solarbank.server.ErrorMessage;
import org.solarbank.server.ValidationMessage;
import org.solarbank.server.dto.CalculateRequest;
import org.solarbank.server.dto.ErrorResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ControllerExceptionHandlerTest {

    @InjectMocks
    private ControllerExceptionHandler handler;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private MethodArgumentNotValidException ex;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void bindingResultException_validationExceptionHandler_expectedErrorResponse() throws NoSuchMethodException {
        when(ex.getBindingResult()).thenThrow(new RuntimeException());

        ResponseEntity<ErrorResponse> response = handler.handleValidationException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse.ErrorDetails errorDetails = response.getBody().getError();

        assertEquals(400, errorDetails.getCode());
        assertEquals(ErrorMessage.BAD_REQUEST.getMessage(), errorDetails.getStatus());
        assertEquals(ErrorMessage.NO_BINDING_RESULT.getMessage(), errorDetails.getMessage());
    }

    @Test
    public void fieldErrorsException_validationExceptionHandler_expectedErrorResponse() throws NoSuchMethodException {
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenThrow(new RuntimeException());

        ResponseEntity<ErrorResponse> response = handler.handleValidationException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse.ErrorDetails errorDetails = response.getBody().getError();

        assertEquals(400, errorDetails.getCode());
        assertEquals(ErrorMessage.BAD_REQUEST.getMessage(), errorDetails.getStatus());
        assertEquals(ErrorMessage.NO_FIELD_ERRORS.getMessage(), errorDetails.getMessage());
    }

    @Test
    public void noFieldErrors_validationExceptionHandler_expectedErrorResponse() throws NoSuchMethodException {
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(Collections.emptyList());

        ResponseEntity<ErrorResponse> response = handler.handleValidationException(ex);

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
                ValidationMessage.PANEL_EFF_POSITIVE.getMessage()
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
        assertEquals(ValidationMessage.PANEL_EFF_POSITIVE.getMessage()+ "; ", errorDetails.getMessage());
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
}