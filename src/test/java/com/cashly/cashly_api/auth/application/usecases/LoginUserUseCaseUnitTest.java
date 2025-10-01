package com.cashly.cashly_api.auth.application.usecases;

import com.cashly.cashly_api.auth.application.dto.AuthenticationResponse;
import com.cashly.cashly_api.auth.application.dto.LoginRequest;
import com.cashly.cashly_api.auth.application.ports.PasswordEncoder;
import com.cashly.cashly_api.auth.application.ports.RefreshTokenRepository;
import com.cashly.cashly_api.auth.application.ports.TokenService;
import com.cashly.cashly_api.auth.application.ports.UserRepository;
import com.cashly.cashly_api.auth.domain.entities.RefreshToken;
import com.cashly.cashly_api.auth.domain.entities.User;
import com.cashly.cashly_api.auth.domain.services.AuthenticationDomainService;
import com.cashly.cashly_api.auth.domain.valueobjects.*;
import com.cashly.cashly_api.shared.exceptions.InvalidCredentialsException;
import com.cashly.cashly_api.shared.exceptions.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("LoginUserUseCase Unit Tests")
class LoginUserUseCaseUnitTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private TokenService tokenService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationDomainService authenticationDomainService;

    @InjectMocks
    private LoginUserUseCase loginUserUseCase;

    private LoginRequest validLoginRequest;
    private User testUser;
    private RefreshToken testRefreshToken;

    @BeforeEach
    void setUp() {
        validLoginRequest = new LoginRequest("test@example.com", "SecurePass123!");

        UserId userId = UserId.generate();
        Email email = new Email("test@example.com");
        Password password = Password.fromHash("$2a$10$hashedPassword");
        UserProfile profile = new UserProfile("John", "Doe");

        testUser = new User(userId, email, password, profile);

        testRefreshToken = new RefreshToken(
            RefreshTokenId.generate(),
            userId,
            UUID.randomUUID().toString(),
            LocalDateTime.now().plusDays(7)
        );
    }

    @Test
    @DisplayName("should_AuthenticateUser_When_ValidCredentialsProvided")
    void should_AuthenticateUser_When_ValidCredentialsProvided() {
        when(userRepository.findByEmail(any(Email.class))).thenReturn(Optional.of(testUser));
        when(authenticationDomainService.canUserAuthenticate(testUser)).thenReturn(true);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(tokenService.generateAccessToken(testUser)).thenReturn("access-token");
        when(tokenService.generateRefreshToken(testUser)).thenReturn(testRefreshToken);
        when(tokenService.getAccessTokenExpiration()).thenReturn(900000L);
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(testRefreshToken);

        AuthenticationResponse response = loginUserUseCase.execute(validLoginRequest);

        assertNotNull(response);
        assertEquals("access-token", response.accessToken());
        assertEquals(testRefreshToken.getToken(), response.refreshToken());
        assertEquals(900000L, response.expiresIn());
        assertNotNull(response.user());
        assertEquals("test@example.com", response.user().email());

        verify(userRepository, times(1)).findByEmail(any(Email.class));
        verify(authenticationDomainService, times(1)).canUserAuthenticate(testUser);
        verify(passwordEncoder, times(1)).matches(anyString(), anyString());
        verify(tokenService, times(1)).generateAccessToken(testUser);
        verify(tokenService, times(1)).generateRefreshToken(testUser);
        verify(refreshTokenRepository, times(1)).save(any(RefreshToken.class));
    }

    @Test
    @DisplayName("should_ThrowException_When_UserNotFound")
    void should_ThrowException_When_UserNotFound() {
        when(userRepository.findByEmail(any(Email.class))).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(
            UserNotFoundException.class,
            () -> loginUserUseCase.execute(validLoginRequest)
        );

        assertEquals("User not found with email: test@example.com", exception.getMessage());
        verify(userRepository, times(1)).findByEmail(any(Email.class));
        verify(authenticationDomainService, never()).canUserAuthenticate(any());
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(tokenService, never()).generateAccessToken(any());
    }

    @Test
    @DisplayName("should_ThrowException_When_UserIsNotActive")
    void should_ThrowException_When_UserIsNotActive() {
        when(userRepository.findByEmail(any(Email.class))).thenReturn(Optional.of(testUser));
        when(authenticationDomainService.canUserAuthenticate(testUser)).thenReturn(false);

        InvalidCredentialsException exception = assertThrows(
            InvalidCredentialsException.class,
            () -> loginUserUseCase.execute(validLoginRequest)
        );

        assertEquals("User account is not active", exception.getMessage());
        verify(userRepository, times(1)).findByEmail(any(Email.class));
        verify(authenticationDomainService, times(1)).canUserAuthenticate(testUser);
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(tokenService, never()).generateAccessToken(any());
    }

    @Test
    @DisplayName("should_ThrowException_When_PasswordIsInvalid")
    void should_ThrowException_When_PasswordIsInvalid() {
        when(userRepository.findByEmail(any(Email.class))).thenReturn(Optional.of(testUser));
        when(authenticationDomainService.canUserAuthenticate(testUser)).thenReturn(true);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        InvalidCredentialsException exception = assertThrows(
            InvalidCredentialsException.class,
            () -> loginUserUseCase.execute(validLoginRequest)
        );

        assertEquals("Invalid email or password", exception.getMessage());
        verify(userRepository, times(1)).findByEmail(any(Email.class));
        verify(authenticationDomainService, times(1)).canUserAuthenticate(testUser);
        verify(passwordEncoder, times(1)).matches(anyString(), anyString());
        verify(tokenService, never()).generateAccessToken(any());
    }

    @Test
    @DisplayName("should_GenerateAccessToken_When_LoginSuccessful")
    void should_GenerateAccessToken_When_LoginSuccessful() {
        when(userRepository.findByEmail(any(Email.class))).thenReturn(Optional.of(testUser));
        when(authenticationDomainService.canUserAuthenticate(testUser)).thenReturn(true);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(tokenService.generateAccessToken(testUser)).thenReturn("access-token");
        when(tokenService.generateRefreshToken(testUser)).thenReturn(testRefreshToken);
        when(tokenService.getAccessTokenExpiration()).thenReturn(900000L);
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(testRefreshToken);

        AuthenticationResponse response = loginUserUseCase.execute(validLoginRequest);

        assertEquals("access-token", response.accessToken());
        verify(tokenService, times(1)).generateAccessToken(testUser);
    }

    @Test
    @DisplayName("should_GenerateRefreshToken_When_LoginSuccessful")
    void should_GenerateRefreshToken_When_LoginSuccessful() {
        when(userRepository.findByEmail(any(Email.class))).thenReturn(Optional.of(testUser));
        when(authenticationDomainService.canUserAuthenticate(testUser)).thenReturn(true);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(tokenService.generateAccessToken(testUser)).thenReturn("access-token");
        when(tokenService.generateRefreshToken(testUser)).thenReturn(testRefreshToken);
        when(tokenService.getAccessTokenExpiration()).thenReturn(900000L);
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(testRefreshToken);

        AuthenticationResponse response = loginUserUseCase.execute(validLoginRequest);

        assertEquals(testRefreshToken.getToken(), response.refreshToken());
        verify(tokenService, times(1)).generateRefreshToken(testUser);
        verify(refreshTokenRepository, times(1)).save(testRefreshToken);
    }

    @Test
    @DisplayName("should_SaveRefreshToken_When_LoginSuccessful")
    void should_SaveRefreshToken_When_LoginSuccessful() {
        when(userRepository.findByEmail(any(Email.class))).thenReturn(Optional.of(testUser));
        when(authenticationDomainService.canUserAuthenticate(testUser)).thenReturn(true);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(tokenService.generateAccessToken(testUser)).thenReturn("access-token");
        when(tokenService.generateRefreshToken(testUser)).thenReturn(testRefreshToken);
        when(tokenService.getAccessTokenExpiration()).thenReturn(900000L);
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(testRefreshToken);

        loginUserUseCase.execute(validLoginRequest);

        verify(refreshTokenRepository, times(1)).save(testRefreshToken);
    }

    @Test
    @DisplayName("should_ReturnUserData_When_LoginSuccessful")
    void should_ReturnUserData_When_LoginSuccessful() {
        when(userRepository.findByEmail(any(Email.class))).thenReturn(Optional.of(testUser));
        when(authenticationDomainService.canUserAuthenticate(testUser)).thenReturn(true);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(tokenService.generateAccessToken(testUser)).thenReturn("access-token");
        when(tokenService.generateRefreshToken(testUser)).thenReturn(testRefreshToken);
        when(tokenService.getAccessTokenExpiration()).thenReturn(900000L);
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(testRefreshToken);

        AuthenticationResponse response = loginUserUseCase.execute(validLoginRequest);

        assertNotNull(response.user());
        assertEquals("test@example.com", response.user().email());
        assertEquals("John", response.user().firstName());
        assertEquals("Doe", response.user().lastName());
    }

    @Test
    @DisplayName("should_NormalizeEmail_When_Authenticating")
    void should_NormalizeEmail_When_Authenticating() {
        LoginRequest upperCaseRequest = new LoginRequest("TEST@EXAMPLE.COM", "SecurePass123!");

        when(userRepository.findByEmail(any(Email.class))).thenReturn(Optional.of(testUser));
        when(authenticationDomainService.canUserAuthenticate(testUser)).thenReturn(true);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(tokenService.generateAccessToken(testUser)).thenReturn("access-token");
        when(tokenService.generateRefreshToken(testUser)).thenReturn(testRefreshToken);
        when(tokenService.getAccessTokenExpiration()).thenReturn(900000L);
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(testRefreshToken);

        AuthenticationResponse response = loginUserUseCase.execute(upperCaseRequest);

        assertNotNull(response);
        assertEquals("test@example.com", response.user().email());
    }
}
