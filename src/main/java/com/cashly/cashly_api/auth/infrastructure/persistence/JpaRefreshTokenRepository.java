package com.cashly.cashly_api.auth.infrastructure.persistence;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cashly.cashly_api.auth.application.ports.RefreshTokenRepository;
import com.cashly.cashly_api.auth.domain.entities.RefreshToken;
import com.cashly.cashly_api.auth.domain.valueobjects.UserId;


@Component
public class JpaRefreshTokenRepository implements RefreshTokenRepository {

    private final SpringDataRefreshTokenRepository springDataRefreshTokenRepository;

    public JpaRefreshTokenRepository(SpringDataRefreshTokenRepository springDataRefreshTokenRepository) {
        this.springDataRefreshTokenRepository = springDataRefreshTokenRepository;
    }

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        if (refreshToken == null) {
            throw new IllegalArgumentException("RefreshToken cannot be null");
        }

        String tokenIdStr = refreshToken.getId().getValue().toString();
        Optional<RefreshTokenEntity> existingEntity = springDataRefreshTokenRepository.findById(tokenIdStr);

        RefreshTokenEntity entity;
        if (existingEntity.isPresent()) {
            entity = existingEntity.get();
            entity.updateFromDomain(refreshToken);
        } else {
            entity = RefreshTokenEntity.fromDomain(refreshToken);
        }

        RefreshTokenEntity savedEntity = springDataRefreshTokenRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            throw new IllegalArgumentException("Token cannot be null or empty");
        }

        return springDataRefreshTokenRepository.findByToken(token)
            .map(RefreshTokenEntity::toDomain);
    }

    @Override
    public List<RefreshToken> findByUserId(UserId userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        return springDataRefreshTokenRepository.findByUserId(userId.getValue().toString())
            .stream()
            .map(RefreshTokenEntity::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void revokeAllByUserId(UserId userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        springDataRefreshTokenRepository.revokeAllUserTokens(userId.getValue().toString());
    }

    @Override
    @Transactional
    public void deleteExpiredTokens() {
        springDataRefreshTokenRepository.deleteExpiredTokens(LocalDateTime.now());
    }
}
