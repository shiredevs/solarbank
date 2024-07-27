package org.solarbank.server.unit;

import java.lang.reflect.Method;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.solarbank.server.ControllerExceptionHandler;
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

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void invalidInput_validationExceptionHandler_expectedErrorResponse() throws NoSuchMethodException {

        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError(
                "CalculateRequest",
                "panelEfficiency",
                "Panel efficiency must be between 0 and 1"
        );

        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        Method method = CalculateRequest.class.getMethod("getPanelEfficiency");
        MethodParameter methodParameter = new MethodParameter(method, -1);

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(methodParameter, bindingResult);

        ResponseEntity<ErrorResponse> response = handler.handleValidationException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse.ErrorDetails errorDetails = response.getBody().getError();
        System.out.println(errorDetails);
        assertEquals(400, errorDetails.getCode());
        assertEquals("Bad Request", errorDetails.getStatus());
        assertEquals("Panel efficiency must be between 0 and 1; ", errorDetails.getMessage());
    }

    @Test
    public void exception_defaultExceptionHandler_expectedServerError() {

        Exception exception = new Exception();

        ResponseEntity<ErrorResponse> response = handler.defaultExceptionHandler(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        ErrorResponse.ErrorDetails errorDetails = response.getBody().getError();
        System.out.println(errorDetails);
        assertEquals(500, errorDetails.getCode());
        assertEquals("Internal Server Error", errorDetails.getStatus());
        assertEquals("An unexpected error occurred. Please try again later.", errorDetails.getMessage());
    }
}