package com.cashly.cashly_api.auth.application.dto;

import com.cashly.cashly_api.auth.domain.entities.User;

import java.time.LocalDateTime;

public record UserResponse(
    String id,
    String email,
    String firstName,
    String lastName,
    boolean active,
    boolean emailVerified,
    LocalDateTime createdAt
) {
    public static UserResponse from(User user) {
        return new UserResponse(
            user.getId().getValue().toString(),
            user.getEmail().getValue(),
            user.getProfile().getFirstName(),
            user.getProfile().getLastName(),
            user.isActive(),
            user.isEmailVerified(),
            user.getCreatedAt()
        );
    }
}
