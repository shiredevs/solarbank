package org.solarbank.server.unit;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;
import org.solarbank.server.ControllerExceptionHandler;
import org.solarbank.server.ErrorMessage;
import org.solarbank.server.ValidationMessage;
import org.solarbank.server.dto.ErrorResponse;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.http.MockHttpInputMessage;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ControllerExceptionHandlerTest {

    private ControllerExceptionHandler handler;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private MethodArgumentNotValidException ex;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    public void setUp() {
        handler = new ControllerExceptionHandler();
    }

    @Test
    public void validationFails_noFieldErrorsCreated_noFieldErrorResponseReturned() {
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(Collections.emptyList());

        ResponseEntity<ErrorResponse> response = handler.handleValidationException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse.ErrorDetails errorDetails = Objects.requireNonNull(response.getBody()).getError();
        assertEquals(400, errorDetails.getCode());
        assertEquals(HttpStatus.BAD_REQUEST.getReasonPhrase(), errorDetails.getStatus());
        assertEquals(ErrorMessage.NO_FIELD_ERRORS.getMessage(), errorDetails.getMessage());
    }

    @Test
    public void validationFails_singleFieldError_singleInvalidUserInputResponseReturned() {
        FieldError fieldError = new FieldError(
            "CalculateRequest",
            "panelEfficiency",
            ValidationMessage.PANEL_EFF_POSITIVE
        );

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        ResponseEntity<ErrorResponse> response = handler.handleValidationException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse.ErrorDetails errorDetails = Objects.requireNonNull(response.getBody()).getError();
        assertEquals(400, errorDetails.getCode());
        assertEquals(HttpStatus.BAD_REQUEST.getReasonPhrase(), errorDetails.getStatus());
        assertEquals(ValidationMessage.PANEL_EFF_POSITIVE, errorDetails.getMessage());
    }

    @Test
    public void validationFails_nullValidationErrorMessage_noErrorResponseReturned() {
        FieldError fieldError = new FieldError(
            "CalculateRequest",
            "panelEfficiency",
            null
        );

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        ResponseEntity<ErrorResponse> response = handler.handleValidationException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse.ErrorDetails errorDetails = Objects.requireNonNull(response.getBody()).getError();
        assertEquals(400, errorDetails.getCode());
        assertEquals(HttpStatus.BAD_REQUEST.getReasonPhrase(), errorDetails.getStatus());
        assertEquals(ErrorMessage.NO_ERROR_MESSAGE.getMessage(), errorDetails.getMessage());
    }

    @Test
    public void validationFails_multipleFieldErrors_multipleInvalidUserInputResponseReturned() {
        List<FieldError> fieldErrors = List.of(
            new FieldError("CalculateRequest", "panelEfficiency", ValidationMessage.PANEL_EFF_POSITIVE),
            new FieldError("CalculateRequest", "location", ValidationMessage.LOCATION_NULL)
        );

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);

        ResponseEntity<ErrorResponse> response = handler.handleValidationException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse.ErrorDetails errorDetails = Objects.requireNonNull(response.getBody()).getError();

        assertEquals(400, errorDetails.getCode());
        assertEquals(HttpStatus.BAD_REQUEST.getReasonPhrase(), errorDetails.getStatus());
        assertEquals(
            ValidationMessage.PANEL_EFF_POSITIVE + "; " + ValidationMessage.LOCATION_NULL ,
            errorDetails.getMessage()
        );
    }

    @Test
    public void defaultException_defaultExceptionThrown_internalServerErrorResponseReturned() {
        Exception exception = new Exception();

        ResponseEntity<ErrorResponse> response = handler.defaultExceptionHandler(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        ErrorResponse.ErrorDetails errorDetails = Objects.requireNonNull(response.getBody()).getError();

        assertEquals(500, errorDetails.getCode());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), errorDetails.getStatus());
        assertEquals(ErrorMessage.INTERNAL_SERVER_ERROR.getMessage(), errorDetails.getMessage());
    }

    @Test
    public void messageNotReadableException_nullRequestBody_emptyUserRequestResponse() {
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException(
            "Invalid request body",
            new MockHttpInputMessage(new byte[0])
        );

        ResponseEntity<ErrorResponse> response = handler.handleHttpMessageNotReadableException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse.ErrorDetails errorDetails = Objects.requireNonNull(response.getBody()).getError();
        assertEquals(HttpStatus.BAD_REQUEST.value(), errorDetails.getCode());
        assertEquals(HttpStatus.BAD_REQUEST.getReasonPhrase(), errorDetails.getStatus());
        assertEquals(ValidationMessage.REQUEST_NULL, errorDetails.getMessage());
    }

    @Test
    public void noResourceFoundException_noResourceFoundExceptionThrown_pageNotFoundResponseReturned() {
        String requestPath = "/nonexistent-page";
        NoResourceFoundException exception = new NoResourceFoundException(HttpMethod.POST, requestPath);

        when(request.getRequestURI()).thenReturn(requestPath);

        ResponseEntity<ErrorResponse> response = handler.handleNoResourceFoundException(exception, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorResponse.ErrorDetails errorDetails = Objects.requireNonNull(response.getBody()).getError();
        assertEquals(404, errorDetails.getCode());
        assertEquals(HttpStatus.NOT_FOUND.getReasonPhrase(), errorDetails.getStatus());
        assertEquals(ErrorMessage.NOT_FOUND.getMessage() + requestPath, errorDetails.getMessage());
    }
}
