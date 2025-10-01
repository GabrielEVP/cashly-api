package com.cashly.cashly_api.auth.application.usecases;

import com.cashly.cashly_api.auth.application.dto.AuthenticationResponse;
import com.cashly.cashly_api.auth.application.dto.RefreshTokenRequest;
import com.cashly.cashly_api.auth.application.ports.RefreshTokenRepository;
import com.cashly.cashly_api.auth.application.ports.TokenService;
import com.cashly.cashly_api.auth.application.ports.UserRepository;
import com.cashly.cashly_api.auth.domain.entities.RefreshToken;
import com.cashly.cashly_api.auth.domain.entities.User;
import com.cashly.cashly_api.auth.domain.valueobjects.*;
import com.cashly.cashly_api.shared.exceptions.InvalidTokenException;
import com.cashly.cashly_api.shared.exceptions.TokenExpiredException;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RefreshTokenUseCase Unit Tests")
class RefreshTokenUseCaseUnitTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private RefreshTokenUseCase refreshTokenUseCase;

    private RefreshTokenRequest validRequest;
    private RefreshToken validRefreshToken;
    private User testUser;

    @BeforeEach
    void setUp() {
        String tokenString = UUID.randomUUID().toString();
        validRequest = new RefreshTokenRequest(tokenString);

        UserId userId = UserId.generate();
        validRefreshToken = new RefreshToken(
            RefreshTokenId.generate(),
            userId,
            tokenString,
            LocalDateTime.now().plusDays(7)
        );

        Email email = new Email("test@example.com");
        Password password = Password.fromHash("$2a$10$hashedPassword");
        UserProfile profile = new UserProfile("John", "Doe");
        testUser = new User(userId, email, password, profile);
    }

    @Test
    @DisplayName("should_RefreshTokens_When_ValidRefreshTokenProvided")
    void should_RefreshTokens_When_ValidRefreshTokenProvided() {
        when(refreshTokenRepository.findByToken(validRequest.refreshToken()))
            .thenReturn(Optional.of(validRefreshToken));
        when(userRepository.findById(any(UserId.class))).thenReturn(Optional.of(testUser));
        when(tokenService.generateAccessToken(testUser)).thenReturn("new-access-token");
        when(tokenService.generateRefreshToken(testUser)).thenReturn(validRefreshToken);
        when(tokenService.getAccessTokenExpiration()).thenReturn(900000L);
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(validRefreshToken);

        AuthenticationResponse response = refreshTokenUseCase.execute(validRequest);

        assertNotNull(response);
        assertEquals("new-access-token", response.accessToken());
        assertEquals(validRefreshToken.getToken(), response.refreshToken());
        verify(refreshTokenRepository, times(1)).findByToken(validRequest.refreshToken());
        verify(userRepository, times(1)).findById(any(UserId.class));
        verify(tokenService, times(1)).generateAccessToken(testUser);
        verify(tokenService, times(1)).generateRefreshToken(testUser);
        verify(refreshTokenRepository, times(2)).save(any(RefreshToken.class)); 
    }

    @Test
    @DisplayName("should_ThrowException_When_RefreshTokenNotFound")
    void should_ThrowException_When_RefreshTokenNotFound() {
        when(refreshTokenRepository.findByToken(validRequest.refreshToken()))
            .thenReturn(Optional.empty());

        InvalidTokenException exception = assertThrows(
            InvalidTokenException.class,
            () -> refreshTokenUseCase.execute(validRequest)
        );

        assertEquals("Invalid refresh token", exception.getMessage());
        verify(refreshTokenRepository, times(1)).findByToken(validRequest.refreshToken());
        verify(userRepository, never()).findById(any());
        verify(tokenService, never()).generateAccessToken(any());
    }

    @Test
    @DisplayName("should_ThrowException_When_RefreshTokenExpired")
    void should_ThrowException_When_RefreshTokenExpired() {
        RefreshToken token = new RefreshToken(
            RefreshTokenId.generate(),
            testUser.getId(),
            UUID.randomUUID().toString(),
            LocalDateTime.now().plusDays(1) 
        );

        try {
            java.lang.reflect.Field expiresAtField = RefreshToken.class.getDeclaredField("expiresAt");
            expiresAtField.setAccessible(true);
            expiresAtField.set(token, LocalDateTime.now().minusDays(1)); 
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        when(refreshTokenRepository.findByToken(validRequest.refreshToken()))
            .thenReturn(Optional.of(token));

        InvalidTokenException exception = assertThrows(
            InvalidTokenException.class,
            () -> refreshTokenUseCase.execute(validRequest)
        );

        assertEquals("Refresh token is expired or revoked", exception.getMessage());
        verify(refreshTokenRepository, times(1)).findByToken(validRequest.refreshToken());
        verify(userRepository, never()).findById(any());
    }

    @Test
    @DisplayName("should_ThrowException_When_RefreshTokenRevoked")
    void should_ThrowException_When_RefreshTokenRevoked() {
        validRefreshToken.revoke();
        when(refreshTokenRepository.findByToken(validRequest.refreshToken()))
            .thenReturn(Optional.of(validRefreshToken));

        InvalidTokenException exception = assertThrows(
            InvalidTokenException.class,
            () -> refreshTokenUseCase.execute(validRequest)
        );

        assertEquals("Refresh token is expired or revoked", exception.getMessage());
        verify(refreshTokenRepository, times(1)).findByToken(validRequest.refreshToken());
        verify(userRepository, never()).findById(any());
    }

    @Test
    @DisplayName("should_ThrowException_When_UserNotFound")
    void should_ThrowException_When_UserNotFound() {
        when(refreshTokenRepository.findByToken(validRequest.refreshToken()))
            .thenReturn(Optional.of(validRefreshToken));
        when(userRepository.findById(any(UserId.class))).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(
            UserNotFoundException.class,
            () -> refreshTokenUseCase.execute(validRequest)
        );

        assertEquals("User not found", exception.getMessage());
        verify(refreshTokenRepository, times(1)).findByToken(validRequest.refreshToken());
        verify(userRepository, times(1)).findById(any(UserId.class));
        verify(tokenService, never()).generateAccessToken(any());
    }

    @Test
    @DisplayName("should_GenerateNewAccessToken_When_RefreshSuccessful")
    void should_GenerateNewAccessToken_When_RefreshSuccessful() {
        when(refreshTokenRepository.findByToken(validRequest.refreshToken()))
            .thenReturn(Optional.of(validRefreshToken));
        when(userRepository.findById(any(UserId.class))).thenReturn(Optional.of(testUser));
        when(tokenService.generateAccessToken(testUser)).thenReturn("new-access-token");
        when(tokenService.generateRefreshToken(testUser)).thenReturn(validRefreshToken);
        when(tokenService.getAccessTokenExpiration()).thenReturn(900000L);
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(validRefreshToken);

        AuthenticationResponse response = refreshTokenUseCase.execute(validRequest);

        assertEquals("new-access-token", response.accessToken());
        verify(tokenService, times(1)).generateAccessToken(testUser);
    }

    @Test
    @DisplayName("should_GenerateNewRefreshToken_When_RefreshSuccessful")
    void should_GenerateNewRefreshToken_When_RefreshSuccessful() {
        RefreshToken newRefreshToken = new RefreshToken(
            RefreshTokenId.generate(),
            testUser.getId(),
            UUID.randomUUID().toString(),
            LocalDateTime.now().plusDays(7)
        );

        when(refreshTokenRepository.findByToken(validRequest.refreshToken()))
            .thenReturn(Optional.of(validRefreshToken));
        when(userRepository.findById(any(UserId.class))).thenReturn(Optional.of(testUser));
        when(tokenService.generateAccessToken(testUser)).thenReturn("new-access-token");
        when(tokenService.generateRefreshToken(testUser)).thenReturn(newRefreshToken);
        when(tokenService.getAccessTokenExpiration()).thenReturn(900000L);
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(newRefreshToken);

        AuthenticationResponse response = refreshTokenUseCase.execute(validRequest);

        assertEquals(newRefreshToken.getToken(), response.refreshToken());
        verify(tokenService, times(1)).generateRefreshToken(testUser);
        verify(refreshTokenRepository, times(1)).save(newRefreshToken);
    }
}
