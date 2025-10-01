package com.cashly.cashly_api.auth.infrastructure.persistence;

import java.time.LocalDateTime;
import java.util.UUID;

import com.cashly.cashly_api.auth.domain.entities.RefreshToken;
import com.cashly.cashly_api.auth.domain.valueobjects.RefreshTokenId;
import com.cashly.cashly_api.auth.domain.valueobjects.UserId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "refresh_tokens", indexes = {
    @Index(name = "idx_user_id", columnList = "user_id"),
    @Index(name = "idx_token", columnList = "token"),
    @Index(name = "idx_expires_at", columnList = "expires_at"),
    @Index(name = "idx_revoked", columnList = "revoked")
})
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class RefreshTokenEntity {

    @Id
    @Column(name = "id", length = 36, nullable = false)
    @EqualsAndHashCode.Include
    private String id;

    @Column(name = "user_id", length = 36, nullable = false)
    private String userId;

    @Column(name = "token", length = 500, nullable = false, unique = true)
    private String token;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "revoked", nullable = false)
    private boolean revoked;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public static RefreshTokenEntity fromDomain(RefreshToken refreshToken) {
        if (refreshToken == null) {
            throw new IllegalArgumentException("RefreshToken cannot be null");
        }

        RefreshTokenEntity entity = new RefreshTokenEntity();
        entity.id = refreshToken.getId().getValue().toString();
        entity.userId = refreshToken.getUserId().getValue().toString();
        entity.token = refreshToken.getToken();
        entity.expiresAt = refreshToken.getExpiresAt();
        entity.revoked = refreshToken.isRevoked();
        entity.createdAt = refreshToken.getCreatedAt();

        return entity;
    }

    public void updateFromDomain(RefreshToken refreshToken) {
        if (refreshToken == null) {
            throw new IllegalArgumentException("RefreshToken cannot be null");
        }

        this.revoked = refreshToken.isRevoked();
    }

    public RefreshToken toDomain() {
        RefreshTokenId tokenId = new RefreshTokenId(UUID.fromString(this.id));
        UserId domainUserId = new UserId(UUID.fromString(this.userId));

        LocalDateTime safeExpiresAt = this.expiresAt.isBefore(LocalDateTime.now())
            ? LocalDateTime.now().plusDays(1)
            : this.expiresAt;

        RefreshToken refreshToken = new RefreshToken(
            tokenId,
            domainUserId,
            this.token,
            safeExpiresAt
        );

        try {
            java.lang.reflect.Field revokedField = RefreshToken.class.getDeclaredField("revoked");
            java.lang.reflect.Field createdAtField = RefreshToken.class.getDeclaredField("createdAt");
            java.lang.reflect.Field expiresAtField = RefreshToken.class.getDeclaredField("expiresAt");

            revokedField.setAccessible(true);
            createdAtField.setAccessible(true);
            expiresAtField.setAccessible(true);

            revokedField.set(refreshToken, this.revoked);
            createdAtField.set(refreshToken, this.createdAt);
            expiresAtField.set(refreshToken, this.expiresAt);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to set fields on domain entity", e);
        }

        return refreshToken;
    }
}
