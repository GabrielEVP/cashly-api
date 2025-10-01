package com.cashly.cashly_api.auth.application.ports;

import com.cashly.cashly_api.auth.domain.entities.RefreshToken;
import com.cashly.cashly_api.auth.domain.valueobjects.UserId;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository {

    RefreshToken save(RefreshToken refreshToken);

    Optional<RefreshToken> findByToken(String token);

    List<RefreshToken> findByUserId(UserId userId);

    void revokeAllByUserId(UserId userId);

    void deleteExpiredTokens();
}
