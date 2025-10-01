package com.cashly.cashly_api.auth.application.ports;

import com.cashly.cashly_api.auth.domain.entities.RefreshToken;
import com.cashly.cashly_api.auth.domain.entities.User;
import com.cashly.cashly_api.auth.domain.valueobjects.Email;
import com.cashly.cashly_api.auth.domain.valueobjects.UserId;

import java.time.LocalDateTime;

public interface TokenService {

    String generateAccessToken(User user);

    RefreshToken generateRefreshToken(User user);

    boolean validateToken(String token);

    UserId extractUserId(String token);

    Email extractEmail(String token);

    LocalDateTime getExpirationDate(String token);

    long getAccessTokenExpiration();

    long getRefreshTokenExpiration();
}
