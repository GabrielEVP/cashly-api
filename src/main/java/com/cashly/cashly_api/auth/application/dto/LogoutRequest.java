package com.cashly.cashly_api.auth.application.dto;

public record LogoutRequest(
    String refreshToken
) {
    public LogoutRequest {
        if (refreshToken == null || refreshToken.trim().isEmpty()) {
            throw new IllegalArgumentException("Refresh token is required");
        }
    }
}
