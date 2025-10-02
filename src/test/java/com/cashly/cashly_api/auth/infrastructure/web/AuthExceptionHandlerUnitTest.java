package com.cashly.cashly_api.auth.infrastructure.web;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.cashly.cashly_api.shared.exceptions.DuplicateEmailException;
import com.cashly.cashly_api.shared.exceptions.InvalidCredentialsException;
import com.cashly.cashly_api.shared.exceptions.InvalidTokenException;
import com.cashly.cashly_api.shared.exceptions.TokenExpiredException;
import com.cashly.cashly_api.shared.exceptions.UserNotFoundException;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthExceptionHandler Unit Tests")
class AuthExceptionHandlerUnitTest {

    @InjectMocks
    private AuthExceptionHandler authExceptionHandler;

    @Mock
    private MethodArgumentNotValidException validationException;

    @Mock
    private BindingResult bindingResult;

    @Test
    @DisplayName("should_HandleDuplicateEmailException_When_EmailAlreadyExists")
    void should_HandleDuplicateEmailException_When_EmailAlreadyExists() {
        DuplicateEmailException exception = new DuplicateEmailException("test@example.com");

        ResponseEntity<Map<String, Object>> response = authExceptionHandler.handleDuplicateEmail(exception);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(409, response.getBody().get("status"));
        assertEquals("Email already registered", response.getBody().get("error"));
        assertTrue(response.getBody().containsKey("timestamp"));
        assertTrue(response.getBody().containsKey("message"));
    }

    @Test
    @DisplayName("should_HandleUserNotFoundException_When_UserNotFound")
    void should_HandleUserNotFoundException_When_UserNotFound() {
        UserNotFoundException exception = new UserNotFoundException("User not found");

        ResponseEntity<Map<String, Object>> response = authExceptionHandler.handleUserNotFound(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().get("status"));
        assertEquals("User not found", response.getBody().get("error"));
        assertTrue(response.getBody().containsKey("timestamp"));
        assertTrue(response.getBody().containsKey("message"));
    }

    @Test
    @DisplayName("should_HandleInvalidCredentialsException_When_CredentialsInvalid")
    void should_HandleInvalidCredentialsException_When_CredentialsInvalid() {
        InvalidCredentialsException exception = new InvalidCredentialsException("Invalid email or password");

        ResponseEntity<Map<String, Object>> response = authExceptionHandler.handleInvalidCredentials(exception);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(401, response.getBody().get("status"));
        assertEquals("Invalid credentials", response.getBody().get("error"));
        assertTrue(response.getBody().containsKey("timestamp"));
        assertTrue(response.getBody().containsKey("message"));
    }

    @Test
    @DisplayName("should_HandleInvalidTokenException_When_TokenInvalid")
    void should_HandleInvalidTokenException_When_TokenInvalid() {
        InvalidTokenException exception = new InvalidTokenException("Invalid token");

        ResponseEntity<Map<String, Object>> response = authExceptionHandler.handleInvalidToken(exception);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(401, response.getBody().get("status"));
        assertEquals("Invalid token", response.getBody().get("error"));
        assertTrue(response.getBody().containsKey("timestamp"));
        assertTrue(response.getBody().containsKey("message"));
    }

    @Test
    @DisplayName("should_HandleTokenExpiredException_When_TokenExpired")
    void should_HandleTokenExpiredException_When_TokenExpired() {
        TokenExpiredException exception = new TokenExpiredException("Token has expired");

        ResponseEntity<Map<String, Object>> response = authExceptionHandler.handleTokenExpired(exception);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(401, response.getBody().get("status"));
        assertEquals("Token expired", response.getBody().get("error"));
        assertTrue(response.getBody().containsKey("timestamp"));
        assertTrue(response.getBody().containsKey("message"));
    }

    @Test
    @DisplayName("should_HandleIllegalArgumentException_When_ArgumentInvalid")
    void should_HandleIllegalArgumentException_When_ArgumentInvalid() {
        IllegalArgumentException exception = new IllegalArgumentException("Invalid argument provided");

        ResponseEntity<Map<String, Object>> response = authExceptionHandler.handleIllegalArgument(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().get("status"));
        assertEquals("Invalid request", response.getBody().get("error"));
        assertEquals("Invalid argument provided", response.getBody().get("message"));
        assertTrue(response.getBody().containsKey("timestamp"));
    }

    @Test
    @DisplayName("should_HandleValidationException_When_ValidationFails")
    void should_HandleValidationException_When_ValidationFails() {
        FieldError fieldError1 = new FieldError("object", "email", "must be a valid email");
        FieldError fieldError2 = new FieldError("object", "password", "must not be blank");

        when(validationException.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError1, fieldError2));

        ResponseEntity<Map<String, Object>> response = authExceptionHandler.handleValidationErrors(validationException);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().get("status"));
        assertEquals("Validation failed", response.getBody().get("error"));
        assertTrue(response.getBody().containsKey("timestamp"));
        String message = (String) response.getBody().get("message");
        assertTrue(message.contains("email"));
        assertTrue(message.contains("password"));
    }

    @Test
    @DisplayName("should_HandleGenericException_When_UnexpectedErrorOccurs")
    void should_HandleGenericException_When_UnexpectedErrorOccurs() {
        Exception exception = new Exception("Unexpected error");

        ResponseEntity<Map<String, Object>> response = authExceptionHandler.handleGenericException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().get("status"));
        assertEquals("Internal server error", response.getBody().get("error"));
        assertEquals("An unexpected error occurred", response.getBody().get("message"));
        assertTrue(response.getBody().containsKey("timestamp"));
    }

    @Test
    @DisplayName("should_ReturnProperFormat_When_HandlingAnyException")
    void should_ReturnProperFormat_When_HandlingAnyException() {
        DuplicateEmailException exception = new DuplicateEmailException("test@example.com");

        ResponseEntity<Map<String, Object>> response = authExceptionHandler.handleDuplicateEmail(exception);

        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("timestamp"));
        assertTrue(response.getBody().containsKey("status"));
        assertTrue(response.getBody().containsKey("error"));
        assertTrue(response.getBody().containsKey("message"));
        assertEquals(4, response.getBody().size());
    }

    @Test
    @DisplayName("should_ReturnConflictStatus_When_DuplicateEmailOccurs")
    void should_ReturnConflictStatus_When_DuplicateEmailOccurs() {
        DuplicateEmailException exception = new DuplicateEmailException("test@example.com");

        ResponseEntity<Map<String, Object>> response = authExceptionHandler.handleDuplicateEmail(exception);

        assertEquals(HttpStatus.CONFLICT.value(), response.getBody().get("status"));
    }

    @Test
    @DisplayName("should_ReturnNotFoundStatus_When_UserNotFound")
    void should_ReturnNotFoundStatus_When_UserNotFound() {
        UserNotFoundException exception = new UserNotFoundException("User not found");

        ResponseEntity<Map<String, Object>> response = authExceptionHandler.handleUserNotFound(exception);

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().get("status"));
    }

    @Test
    @DisplayName("should_ReturnUnauthorizedStatus_When_InvalidCredentials")
    void should_ReturnUnauthorizedStatus_When_InvalidCredentials() {
        InvalidCredentialsException exception = new InvalidCredentialsException("Invalid credentials");

        ResponseEntity<Map<String, Object>> response = authExceptionHandler.handleInvalidCredentials(exception);

        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getBody().get("status"));
    }
}
