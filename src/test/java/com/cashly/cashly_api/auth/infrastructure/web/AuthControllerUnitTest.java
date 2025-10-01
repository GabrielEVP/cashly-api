package com.cashly.cashly_api.auth.infrastructure.web;

import com.cashly.cashly_api.auth.application.dto.*;
import com.cashly.cashly_api.auth.application.usecases.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthController Unit Tests")
class AuthControllerUnitTest {

    @Mock
    private RegisterUserUseCase registerUserUseCase;

    @Mock
    private LoginUserUseCase loginUserUseCase;

    @Mock
    private RefreshTokenUseCase refreshTokenUseCase;

    @Mock
    private LogoutUserUseCase logoutUserUseCase;

    @InjectMocks
    private AuthController authController;

    private RegisterUserRequest registerRequest;
    private LoginRequest loginRequest;
    private RefreshTokenRequest refreshTokenRequest;
    private LogoutRequest logoutRequest;
    private UserResponse userResponse;
    private AuthenticationResponse authenticationResponse;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterUserRequest(
            "test@example.com",
            "SecurePassword123!",
            "John",
            "Doe"
        );

        loginRequest = new LoginRequest(
            "test@example.com",
            "SecurePassword123!"
        );

        refreshTokenRequest = new RefreshTokenRequest("refresh-token");
        logoutRequest = new LogoutRequest("refresh-token");

        userResponse = new UserResponse(
            "user-id",
            "test@example.com",
            "John",
            "Doe",
            true,
            false,
            java.time.LocalDateTime.now()
        );

        authenticationResponse = new AuthenticationResponse(
            "access-token",
            "refresh-token",
            "Bearer",
            900000L,
            userResponse
        );
    }

    @Test
    @DisplayName("should_RegisterUser_When_ValidRequestProvided")
    void should_RegisterUser_When_ValidRequestProvided() {
        when(registerUserUseCase.execute(any(RegisterUserRequest.class)))
            .thenReturn(userResponse);

        ResponseEntity<UserResponse> response = authController.register(registerRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(userResponse, response.getBody());
        verify(registerUserUseCase, times(1)).execute(registerRequest);
    }

    @Test
    @DisplayName("should_ReturnCreatedStatus_When_RegisteringUser")
    void should_ReturnCreatedStatus_When_RegisteringUser() {
        when(registerUserUseCase.execute(any(RegisterUserRequest.class)))
            .thenReturn(userResponse);

        ResponseEntity<UserResponse> response = authController.register(registerRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    @DisplayName("should_LoginUser_When_ValidCredentialsProvided")
    void should_LoginUser_When_ValidCredentialsProvided() {
        when(loginUserUseCase.execute(any(LoginRequest.class)))
            .thenReturn(authenticationResponse);

        ResponseEntity<AuthenticationResponse> response = authController.login(loginRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(authenticationResponse, response.getBody());
        verify(loginUserUseCase, times(1)).execute(loginRequest);
    }

    @Test
    @DisplayName("should_ReturnOkStatus_When_LoginSuccessful")
    void should_ReturnOkStatus_When_LoginSuccessful() {
        when(loginUserUseCase.execute(any(LoginRequest.class)))
            .thenReturn(authenticationResponse);

        ResponseEntity<AuthenticationResponse> response = authController.login(loginRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("should_RefreshToken_When_ValidRefreshTokenProvided")
    void should_RefreshToken_When_ValidRefreshTokenProvided() {
        when(refreshTokenUseCase.execute(any(RefreshTokenRequest.class)))
            .thenReturn(authenticationResponse);

        ResponseEntity<AuthenticationResponse> response = authController.refresh(refreshTokenRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(authenticationResponse, response.getBody());
        verify(refreshTokenUseCase, times(1)).execute(refreshTokenRequest);
    }

    @Test
    @DisplayName("should_ReturnOkStatus_When_RefreshSuccessful")
    void should_ReturnOkStatus_When_RefreshSuccessful() {
        when(refreshTokenUseCase.execute(any(RefreshTokenRequest.class)))
            .thenReturn(authenticationResponse);

        ResponseEntity<AuthenticationResponse> response = authController.refresh(refreshTokenRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("should_LogoutUser_When_ValidRefreshTokenProvided")
    void should_LogoutUser_When_ValidRefreshTokenProvided() {
        doNothing().when(logoutUserUseCase).execute(any(LogoutRequest.class));

        ResponseEntity<Void> response = authController.logout(logoutRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(logoutUserUseCase, times(1)).execute(logoutRequest);
    }

    @Test
    @DisplayName("should_ReturnNoContentStatus_When_LogoutSuccessful")
    void should_ReturnNoContentStatus_When_LogoutSuccessful() {
        doNothing().when(logoutUserUseCase).execute(any(LogoutRequest.class));

        ResponseEntity<Void> response = authController.logout(logoutRequest);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @DisplayName("should_CallRegisterUseCase_When_RegisterEndpointInvoked")
    void should_CallRegisterUseCase_When_RegisterEndpointInvoked() {
        when(registerUserUseCase.execute(any(RegisterUserRequest.class)))
            .thenReturn(userResponse);

        authController.register(registerRequest);

        verify(registerUserUseCase, times(1)).execute(registerRequest);
    }

    @Test
    @DisplayName("should_CallLoginUseCase_When_LoginEndpointInvoked")
    void should_CallLoginUseCase_When_LoginEndpointInvoked() {
        when(loginUserUseCase.execute(any(LoginRequest.class)))
            .thenReturn(authenticationResponse);

        authController.login(loginRequest);

        verify(loginUserUseCase, times(1)).execute(loginRequest);
    }

    @Test
    @DisplayName("should_CallRefreshTokenUseCase_When_RefreshEndpointInvoked")
    void should_CallRefreshTokenUseCase_When_RefreshEndpointInvoked() {
        when(refreshTokenUseCase.execute(any(RefreshTokenRequest.class)))
            .thenReturn(authenticationResponse);

        authController.refresh(refreshTokenRequest);

        verify(refreshTokenUseCase, times(1)).execute(refreshTokenRequest);
    }

    @Test
    @DisplayName("should_CallLogoutUseCase_When_LogoutEndpointInvoked")
    void should_CallLogoutUseCase_When_LogoutEndpointInvoked() {
        doNothing().when(logoutUserUseCase).execute(any(LogoutRequest.class));

        authController.logout(logoutRequest);

        verify(logoutUserUseCase, times(1)).execute(logoutRequest);
    }
}
