package com.cashly.cashly_api.auth.application.dto;

public record AuthenticationResponse(
    String accessToken,
    String refreshToken,
    String tokenType,
    long expiresIn,
    UserResponse user
) {
    public static AuthenticationResponse of(
        String accessToken,
        String refreshToken,
        long expiresIn,
        UserResponse user
    ) {
        return new AuthenticationResponse(
            accessToken,
            refreshToken,
            "Bearer",
            expiresIn,
            user
        );
    }
}
