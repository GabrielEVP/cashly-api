package com.cashly.cashly_api.auth.domain.entities;

import com.cashly.cashly_api.auth.domain.valueobjects.Email;
import com.cashly.cashly_api.auth.domain.valueobjects.Password;
import com.cashly.cashly_api.auth.domain.valueobjects.UserId;
import com.cashly.cashly_api.auth.domain.valueobjects.UserProfile;

import java.time.LocalDateTime;
import java.util.Objects;

public class User {
    private final UserId id;
    private final Email email;
    private Password password;
    private UserProfile profile;
    private boolean active;
    private boolean emailVerified;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public User(UserId id, Email email, Password password, UserProfile profile) {
        validateParameters(id, email, password, profile);

        this.id = id;
        this.email = email;
        this.password = password;
        this.profile = profile;
        this.active = true;
        this.emailVerified = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    private void validateParameters(UserId id, Email email, Password password, UserProfile profile) {
        if (id == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (email == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }
        if (password == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }
        if (profile == null) {
            throw new IllegalArgumentException("User profile cannot be null");
        }
    }

    public void changePassword(Password newPassword) {
        if (newPassword == null) {
            throw new IllegalArgumentException("New password cannot be null");
        }
        this.password = newPassword;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateProfile(UserProfile newProfile) {
        if (newProfile == null) {
            throw new IllegalArgumentException("User profile cannot be null");
        }
        this.profile = newProfile;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        this.active = true;
        this.updatedAt = LocalDateTime.now();
    }

    public void deactivate() {
        this.active = false;
        this.updatedAt = LocalDateTime.now();
    }

    public void verifyEmail() {
        this.emailVerified = true;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean canAuthenticate() {
        return active;
    }

    public boolean hasEmail(Email email) {
        if (email == null) {
            return false;
        }
        return this.email.equals(email);
    }

    public UserId getId() {
        return id;
    }

    public Email getEmail() {
        return email;
    }

    public Password getPassword() {
        return password;
    }

    public UserProfile getProfile() {
        return profile;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email=" + email +
                ", password=***" +
                ", profile=" + profile +
                ", active=" + active +
                ", emailVerified=" + emailVerified +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
