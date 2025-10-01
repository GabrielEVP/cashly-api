package com.cashly.cashly_api.auth.infrastructure.persistence;

import com.cashly.cashly_api.auth.domain.entities.User;
import com.cashly.cashly_api.auth.domain.valueobjects.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users", indexes = {
    @Index(name = "idx_email", columnList = "email"),
    @Index(name = "idx_active", columnList = "active")
})
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = "passwordHash")
public class UserEntity {

    @Id
    @Column(name = "id", length = 36, nullable = false)
    @EqualsAndHashCode.Include
    private String id;

    @Column(name = "email", length = 255, nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", length = 255, nullable = false)
    private String passwordHash;

    @Column(name = "first_name", length = 100, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 100, nullable = false)
    private String lastName;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "email_verified", nullable = false)
    private boolean emailVerified;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public static UserEntity fromDomain(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        UserEntity entity = new UserEntity();
        entity.id = user.getId().getValue().toString();
        entity.email = user.getEmail().getValue();
        entity.passwordHash = user.getPassword().getHashedValue();
        entity.firstName = user.getProfile().getFirstName();
        entity.lastName = user.getProfile().getLastName();
        entity.active = user.isActive();
        entity.emailVerified = user.isEmailVerified();
        entity.createdAt = user.getCreatedAt();
        entity.updatedAt = user.getUpdatedAt();

        return entity;
    }

    public void updateFromDomain(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        this.passwordHash = user.getPassword().getHashedValue();
        this.firstName = user.getProfile().getFirstName();
        this.lastName = user.getProfile().getLastName();
        this.active = user.isActive();
        this.emailVerified = user.isEmailVerified();
        this.updatedAt = user.getUpdatedAt();
    }

    public User toDomain() {
        UserId userId = new UserId(UUID.fromString(this.id));
        Email domainEmail = new Email(this.email);
        Password domainPassword = Password.fromHash(this.passwordHash);
        UserProfile profile = new UserProfile(this.firstName, this.lastName);

        User user = new User(userId, domainEmail, domainPassword, profile);

        try {
            java.lang.reflect.Field activeField = User.class.getDeclaredField("active");
            java.lang.reflect.Field emailVerifiedField = User.class.getDeclaredField("emailVerified");
            java.lang.reflect.Field createdAtField = User.class.getDeclaredField("createdAt");
            java.lang.reflect.Field updatedAtField = User.class.getDeclaredField("updatedAt");

            activeField.setAccessible(true);
            emailVerifiedField.setAccessible(true);
            createdAtField.setAccessible(true);
            updatedAtField.setAccessible(true);

            activeField.set(user, this.active);
            emailVerifiedField.set(user, this.emailVerified);
            createdAtField.set(user, this.createdAt);
            updatedAtField.set(user, this.updatedAt);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to set fields on domain entity", e);
        }

        return user;
    }
}
