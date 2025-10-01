package com.cashly.cashly_api.auth.infrastructure.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("SpringDataRefreshTokenRepository Integration Tests")
class SpringDataRefreshTokenRepositoryIntegrationTest {

    @Autowired
    private SpringDataRefreshTokenRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    private RefreshTokenEntity testToken;
    private String testUserId;

    @BeforeEach
    void setUp() {
        testUserId = UUID.randomUUID().toString();

        testToken = new RefreshTokenEntity();
        testToken.setId(UUID.randomUUID().toString());
        testToken.setUserId(testUserId);
        testToken.setToken(UUID.randomUUID().toString());
        testToken.setExpiresAt(LocalDateTime.now().plusDays(7));
        testToken.setRevoked(false);
        testToken.setCreatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("should_SaveToken_When_ValidTokenProvided")
    void should_SaveToken_When_ValidTokenProvided() {
        RefreshTokenEntity savedToken = repository.save(testToken);

        assertNotNull(savedToken);
        assertNotNull(savedToken.getId());
        assertEquals(testToken.getToken(), savedToken.getToken());
        assertEquals(testToken.getUserId(), savedToken.getUserId());
    }

    @Test
    @DisplayName("should_FindTokenById_When_TokenExists")
    void should_FindTokenById_When_TokenExists() {
        entityManager.persistAndFlush(testToken);

        Optional<RefreshTokenEntity> foundToken = repository.findById(testToken.getId());

        assertTrue(foundToken.isPresent());
        assertEquals(testToken.getToken(), foundToken.get().getToken());
    }

    @Test
    @DisplayName("should_ReturnEmpty_When_TokenIdNotExists")
    void should_ReturnEmpty_When_TokenIdNotExists() {
        Optional<RefreshTokenEntity> foundToken = repository.findById(UUID.randomUUID().toString());

        assertFalse(foundToken.isPresent());
    }

    @Test
    @DisplayName("should_FindTokenByToken_When_TokenExists")
    void should_FindTokenByToken_When_TokenExists() {
        entityManager.persistAndFlush(testToken);

        Optional<RefreshTokenEntity> foundToken = repository.findByToken(testToken.getToken());

        assertTrue(foundToken.isPresent());
        assertEquals(testToken.getId(), foundToken.get().getId());
    }

    @Test
    @DisplayName("should_ReturnEmpty_When_TokenStringNotExists")
    void should_ReturnEmpty_When_TokenStringNotExists() {
        Optional<RefreshTokenEntity> foundToken = repository.findByToken("nonexistent-token");

        assertFalse(foundToken.isPresent());
    }

    @Test
    @DisplayName("should_FindTokensByUserId_When_UserHasTokens")
    void should_FindTokensByUserId_When_UserHasTokens() {
        RefreshTokenEntity token2 = new RefreshTokenEntity();
        token2.setId(UUID.randomUUID().toString());
        token2.setUserId(testUserId);
        token2.setToken(UUID.randomUUID().toString());
        token2.setExpiresAt(LocalDateTime.now().plusDays(7));
        token2.setRevoked(false);
        token2.setCreatedAt(LocalDateTime.now());

        entityManager.persistAndFlush(testToken);
        entityManager.persistAndFlush(token2);

        List<RefreshTokenEntity> tokens = repository.findByUserId(testUserId);

        assertEquals(2, tokens.size());
    }

    @Test
    @DisplayName("should_ReturnEmptyList_When_UserHasNoTokens")
    void should_ReturnEmptyList_When_UserHasNoTokens() {
        List<RefreshTokenEntity> tokens = repository.findByUserId(UUID.randomUUID().toString());

        assertTrue(tokens.isEmpty());
    }

    @Test
    @DisplayName("should_FindActiveTokens_When_UserHasMixedTokens")
    void should_FindActiveTokens_When_UserHasMixedTokens() {
        RefreshTokenEntity revokedToken = new RefreshTokenEntity();
        revokedToken.setId(UUID.randomUUID().toString());
        revokedToken.setUserId(testUserId);
        revokedToken.setToken(UUID.randomUUID().toString());
        revokedToken.setExpiresAt(LocalDateTime.now().plusDays(7));
        revokedToken.setRevoked(true);
        revokedToken.setCreatedAt(LocalDateTime.now());

        entityManager.persistAndFlush(testToken);
        entityManager.persistAndFlush(revokedToken);

        List<RefreshTokenEntity> activeTokens = repository.findByUserIdAndRevoked(testUserId, false);
        List<RefreshTokenEntity> revokedTokens = repository.findByUserIdAndRevoked(testUserId, true);

        assertEquals(1, activeTokens.size());
        assertEquals(1, revokedTokens.size());
        assertFalse(activeTokens.get(0).isRevoked());
        assertTrue(revokedTokens.get(0).isRevoked());
    }

    @Test
    @Transactional
    @DisplayName("should_DeleteExpiredTokens_When_TokensExpired")
    void should_DeleteExpiredTokens_When_TokensExpired() {
        RefreshTokenEntity expiredToken = new RefreshTokenEntity();
        expiredToken.setId(UUID.randomUUID().toString());
        expiredToken.setUserId(testUserId);
        expiredToken.setToken(UUID.randomUUID().toString());
        expiredToken.setExpiresAt(LocalDateTime.now().minusDays(1));
        expiredToken.setRevoked(false);
        expiredToken.setCreatedAt(LocalDateTime.now().minusDays(8));

        entityManager.persistAndFlush(testToken);
        entityManager.persistAndFlush(expiredToken);
        entityManager.clear();

        int deletedCount = repository.deleteExpiredTokens(LocalDateTime.now());
        entityManager.flush();
        entityManager.clear();

        assertEquals(1, deletedCount);
        assertTrue(repository.findById(testToken.getId()).isPresent());
        assertFalse(repository.findById(expiredToken.getId()).isPresent());
    }

    @Test
    @Transactional
    @DisplayName("should_RevokeAllUserTokens_When_UserHasActiveTokens")
    void should_RevokeAllUserTokens_When_UserHasActiveTokens() {
        RefreshTokenEntity token2 = new RefreshTokenEntity();
        token2.setId(UUID.randomUUID().toString());
        token2.setUserId(testUserId);
        token2.setToken(UUID.randomUUID().toString());
        token2.setExpiresAt(LocalDateTime.now().plusDays(7));
        token2.setRevoked(false);
        token2.setCreatedAt(LocalDateTime.now());

        entityManager.persistAndFlush(testToken);
        entityManager.persistAndFlush(token2);
        entityManager.clear();

        int revokedCount = repository.revokeAllUserTokens(testUserId);
        entityManager.flush();
        entityManager.clear();

        assertEquals(2, revokedCount);

        List<RefreshTokenEntity> tokens = repository.findByUserId(testUserId);
        assertTrue(tokens.stream().allMatch(RefreshTokenEntity::isRevoked));
    }

    @Test
    @DisplayName("should_UpdateToken_When_TokenModified")
    void should_UpdateToken_When_TokenModified() {
        entityManager.persistAndFlush(testToken);

        testToken.setRevoked(true);
        RefreshTokenEntity updatedToken = repository.save(testToken);

        assertTrue(updatedToken.isRevoked());
    }

    @Test
    @DisplayName("should_DeleteToken_When_TokenExists")
    void should_DeleteToken_When_TokenExists() {
        entityManager.persistAndFlush(testToken);

        repository.deleteById(testToken.getId());
        Optional<RefreshTokenEntity> foundToken = repository.findById(testToken.getId());

        assertFalse(foundToken.isPresent());
    }

    @Test
    @DisplayName("should_PreventDuplicateTokens_When_SavingToken")
    void should_PreventDuplicateTokens_When_SavingToken() {
        entityManager.persistAndFlush(testToken);

        RefreshTokenEntity duplicateToken = new RefreshTokenEntity();
        duplicateToken.setId(UUID.randomUUID().toString());
        duplicateToken.setUserId(UUID.randomUUID().toString());
        duplicateToken.setToken(testToken.getToken());
        duplicateToken.setExpiresAt(LocalDateTime.now().plusDays(7));
        duplicateToken.setRevoked(false);
        duplicateToken.setCreatedAt(LocalDateTime.now());

        assertThrows(Exception.class, () -> {
            repository.saveAndFlush(duplicateToken);
        });
    }

    @Test
    @Transactional
    @DisplayName("should_ReturnZero_When_NoExpiredTokensExist")
    void should_ReturnZero_When_NoExpiredTokensExist() {
        entityManager.persistAndFlush(testToken);
        entityManager.flush();

        int deletedCount = repository.deleteExpiredTokens(LocalDateTime.now());

        assertEquals(0, deletedCount);
    }

    @Test
    @Transactional
    @DisplayName("should_ReturnZero_When_UserHasNoActiveTokensToRevoke")
    void should_ReturnZero_When_UserHasNoActiveTokensToRevoke() {
        testToken.setRevoked(true);
        entityManager.persistAndFlush(testToken);
        entityManager.flush();

        int revokedCount = repository.revokeAllUserTokens(testUserId);

        assertEquals(0, revokedCount);
    }
}
