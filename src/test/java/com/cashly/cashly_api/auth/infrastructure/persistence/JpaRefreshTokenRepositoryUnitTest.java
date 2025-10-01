package com.cashly.cashly_api.auth.infrastructure.persistence;

import com.cashly.cashly_api.auth.domain.entities.RefreshToken;
import com.cashly.cashly_api.auth.domain.valueobjects.RefreshTokenId;
import com.cashly.cashly_api.auth.domain.valueobjects.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("JpaRefreshTokenRepository Unit Tests")
class JpaRefreshTokenRepositoryUnitTest {

    @Mock
    private SpringDataRefreshTokenRepository springDataRefreshTokenRepository;

    @InjectMocks
    private JpaRefreshTokenRepository jpaRefreshTokenRepository;

    private RefreshToken testRefreshToken;
    private RefreshTokenEntity testRefreshTokenEntity;
    private UserId testUserId;

    @BeforeEach
    void setUp() {
        testUserId = UserId.generate();
        testRefreshToken = new RefreshToken(
            RefreshTokenId.generate(),
            testUserId,
            UUID.randomUUID().toString(),
            LocalDateTime.now().plusDays(7)
        );

        testRefreshTokenEntity = RefreshTokenEntity.fromDomain(testRefreshToken);
    }

    @Test
    @DisplayName("should_SaveRefreshToken_When_NewTokenProvided")
    void should_SaveRefreshToken_When_NewTokenProvided() {
        when(springDataRefreshTokenRepository.findById(anyString())).thenReturn(Optional.empty());
        when(springDataRefreshTokenRepository.save(any(RefreshTokenEntity.class)))
            .thenReturn(testRefreshTokenEntity);

        RefreshToken savedToken = jpaRefreshTokenRepository.save(testRefreshToken);

        assertNotNull(savedToken);
        assertEquals(testRefreshToken.getId(), savedToken.getId());
        assertEquals(testRefreshToken.getToken(), savedToken.getToken());
        verify(springDataRefreshTokenRepository, times(1)).findById(anyString());
        verify(springDataRefreshTokenRepository, times(1)).save(any(RefreshTokenEntity.class));
    }

    @Test
    @DisplayName("should_UpdateRefreshToken_When_ExistingTokenProvided")
    void should_UpdateRefreshToken_When_ExistingTokenProvided() {
        when(springDataRefreshTokenRepository.findById(anyString()))
            .thenReturn(Optional.of(testRefreshTokenEntity));
        when(springDataRefreshTokenRepository.save(any(RefreshTokenEntity.class)))
            .thenReturn(testRefreshTokenEntity);

        RefreshToken savedToken = jpaRefreshTokenRepository.save(testRefreshToken);

        assertNotNull(savedToken);
        verify(springDataRefreshTokenRepository, times(1)).findById(anyString());
        verify(springDataRefreshTokenRepository, times(1)).save(any(RefreshTokenEntity.class));
    }

    @Test
    @DisplayName("should_ThrowException_When_SavingNullRefreshToken")
    void should_ThrowException_When_SavingNullRefreshToken() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> jpaRefreshTokenRepository.save(null)
        );

        assertEquals("RefreshToken cannot be null", exception.getMessage());
        verify(springDataRefreshTokenRepository, never()).save(any());
    }

    @Test
    @DisplayName("should_FindTokenByString_When_TokenExists")
    void should_FindTokenByString_When_TokenExists() {
        String tokenString = testRefreshToken.getToken();
        when(springDataRefreshTokenRepository.findByToken(tokenString))
            .thenReturn(Optional.of(testRefreshTokenEntity));

        Optional<RefreshToken> foundToken = jpaRefreshTokenRepository.findByToken(tokenString);

        assertTrue(foundToken.isPresent());
        assertEquals(testRefreshToken.getToken(), foundToken.get().getToken());
        verify(springDataRefreshTokenRepository, times(1)).findByToken(anyString());
    }

    @Test
    @DisplayName("should_ReturnEmpty_When_TokenNotFound")
    void should_ReturnEmpty_When_TokenNotFound() {
        String tokenString = "nonexistent-token";
        when(springDataRefreshTokenRepository.findByToken(tokenString))
            .thenReturn(Optional.empty());

        Optional<RefreshToken> foundToken = jpaRefreshTokenRepository.findByToken(tokenString);

        assertFalse(foundToken.isPresent());
        verify(springDataRefreshTokenRepository, times(1)).findByToken(anyString());
    }

    @Test
    @DisplayName("should_ThrowException_When_FindingByNullToken")
    void should_ThrowException_When_FindingByNullToken() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> jpaRefreshTokenRepository.findByToken(null)
        );

        assertEquals("Token cannot be null or empty", exception.getMessage());
        verify(springDataRefreshTokenRepository, never()).findByToken(anyString());
    }

    @Test
    @DisplayName("should_ThrowException_When_FindingByEmptyToken")
    void should_ThrowException_When_FindingByEmptyToken() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> jpaRefreshTokenRepository.findByToken("")
        );

        assertEquals("Token cannot be null or empty", exception.getMessage());
        verify(springDataRefreshTokenRepository, never()).findByToken(anyString());
    }

    @Test
    @DisplayName("should_FindTokensByUserId_When_TokensExist")
    void should_FindTokensByUserId_When_TokensExist() {
        RefreshTokenEntity entity1 = testRefreshTokenEntity;
        RefreshTokenEntity entity2 = RefreshTokenEntity.fromDomain(
            new RefreshToken(
                RefreshTokenId.generate(),
                testUserId,
                UUID.randomUUID().toString(),
                LocalDateTime.now().plusDays(7)
            )
        );

        when(springDataRefreshTokenRepository.findByUserId(testUserId.getValue().toString()))
            .thenReturn(Arrays.asList(entity1, entity2));

        List<RefreshToken> tokens = jpaRefreshTokenRepository.findByUserId(testUserId);

        assertEquals(2, tokens.size());
        verify(springDataRefreshTokenRepository, times(1)).findByUserId(anyString());
    }

    @Test
    @DisplayName("should_ReturnEmptyList_When_NoTokensForUser")
    void should_ReturnEmptyList_When_NoTokensForUser() {
        UserId userId = UserId.generate();
        when(springDataRefreshTokenRepository.findByUserId(userId.getValue().toString()))
            .thenReturn(List.of());

        List<RefreshToken> tokens = jpaRefreshTokenRepository.findByUserId(userId);

        assertTrue(tokens.isEmpty());
        verify(springDataRefreshTokenRepository, times(1)).findByUserId(anyString());
    }

    @Test
    @DisplayName("should_ThrowException_When_FindingByNullUserId")
    void should_ThrowException_When_FindingByNullUserId() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> jpaRefreshTokenRepository.findByUserId(null)
        );

        assertEquals("User ID cannot be null", exception.getMessage());
        verify(springDataRefreshTokenRepository, never()).findByUserId(anyString());
    }

    @Test
    @DisplayName("should_RevokeAllUserTokens_When_UserIdProvided")
    void should_RevokeAllUserTokens_When_UserIdProvided() {
        when(springDataRefreshTokenRepository.revokeAllUserTokens(testUserId.getValue().toString()))
            .thenReturn(2);

        jpaRefreshTokenRepository.revokeAllByUserId(testUserId);

        verify(springDataRefreshTokenRepository, times(1))
            .revokeAllUserTokens(testUserId.getValue().toString());
    }

    @Test
    @DisplayName("should_ThrowException_When_RevokingWithNullUserId")
    void should_ThrowException_When_RevokingWithNullUserId() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> jpaRefreshTokenRepository.revokeAllByUserId(null)
        );

        assertEquals("User ID cannot be null", exception.getMessage());
        verify(springDataRefreshTokenRepository, never()).revokeAllUserTokens(anyString());
    }

    @Test
    @DisplayName("should_DeleteExpiredTokens_When_Called")
    void should_DeleteExpiredTokens_When_Called() {
        when(springDataRefreshTokenRepository.deleteExpiredTokens(any(LocalDateTime.class)))
            .thenReturn(5);

        jpaRefreshTokenRepository.deleteExpiredTokens();

        verify(springDataRefreshTokenRepository, times(1))
            .deleteExpiredTokens(any(LocalDateTime.class));
    }
}
